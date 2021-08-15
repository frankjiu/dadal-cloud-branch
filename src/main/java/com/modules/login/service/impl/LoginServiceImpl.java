package com.modules.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.core.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.modules.gen.model.entity.User;
import com.modules.gen.service.UserService;
import com.modules.login.model.dto.*;
import com.modules.login.model.vo.*;
import com.modules.login.service.AliLoginService;
import com.modules.login.service.LoginService;
import com.modules.login.service.WxLoginService;
import io.jsonwebtoken.Claims;
import jodd.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private HttpServletRequest request;

    @Resource
    private UserService userService;

    @Resource
    private WxLoginService wxLoginService;

    @Resource
    private AliLoginService aliLoginService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IdUtil idUtil;

    @Autowired
    private ObjectMapper mapper;

    /**
     * 执行登录时删除旧token(重设token)
     *
     * @param user
     * @return
     */
    @Override
    public String login(User user) throws Exception {
        // 确认登录, 生成jwt放入redis中保存登录状态
        JWTPayload payload = new JWTPayload();
        // 可以修改对应实体类, 添加参数
        payload.setId(user.getId());
        String token = jwtUtils.createJWT(RandomStrUtil.generateStr(32), payload.toString());
        redisUtil.set(Constant.TOKEN_PREFIX + user.getId(), token, Constant.LOGIN_EXPIRE_TIME);
        // 在redis存储用户信息
        String userInfo = mapper.writeValueAsString(user);
        redisUtil.set(Constant.USER_PREFIX + user.getId(), userInfo, Constant.LOGIN_EXPIRE_TIME);
        return token;
    }

    @Override
    public void logout() {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new CommonException(RespCode.NO_TOKEN.getDescription());
        }
        Claims claims = jwtUtils.parseJWT(token);
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        String id = String.valueOf(payload.getId());
        redisUtil.del(Constant.TOKEN_PREFIX + id);
        redisUtil.del(Constant.USER_PREFIX + id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User insert(User user, BindPhoneDto dto) throws Exception {
        if (user == null) {
            user = new User();
            long userId = DBUtils.nextId();
            user.setId(userId);
            user.setMobile(dto.getPhone());
            user.setCreateTime(System.currentTimeMillis() / 1000);
            user.setCountry(Constant.UserDefaultInfo.COUNTRY);
            user.setProvince(Constant.UserDefaultInfo.PROVINCE);
            user.setCity(Constant.UserDefaultInfo.CITY);
            user.setBirthday(Constant.UserDefaultInfo.BIRTHDAY);
            user.setHeadimgurl(Constant.UserDefaultInfo.HEAD_IMG);
            user.setSex(Constant.UserDefaultInfo.SEX);
            user.setNickname(RandomStrUtil.generateStr(12).toLowerCase());
        }

        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User insert(User user, String mobile) throws Exception {
        if (user == null) {
            user = new User();
            long userId = DBUtils.nextId();
            user.setId(userId);
            user.setMobile(mobile);
            user.setCreateTime(System.currentTimeMillis() / 1000);
            userService.insert(user);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PhoneLoginVo phoneLogin(String mobile) throws Exception {
        // 根据手机查询账号
        User user = find(mobile);
        // 无用户和账号: 创建账号
        user = insert(user, mobile);
        // 执行登录
        String token = login(user);
        PhoneLoginVo vo = new PhoneLoginVo();
        vo.setToken(token);
        if (Constant.Test.TEST_MOBILE.equals(mobile)) {
            return vo;
        }
        return vo;
    }

    @Override
    public NeedOtherVo needOther() throws Exception {
        // 根据用户token获取用户id信息
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new CommonException(RespCode.NO_TOKEN.getDescription());
        }
        Claims claims = jwtUtils.parseJWT(token);
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        String uid = String.valueOf(payload.getId());
        Long userId = Long.valueOf(uid);

        // 根据id查询用户
        User user = userService.findById(userId);
        NeedOtherVo vo = new NeedOtherVo();
        // 当前用户的绑定情况
        if (user == null) {
            log.error(RespCode.USER_NOT_FOUND.getDescription());
            throw new CommonException(RespCode.USER_NOT_FOUND.getDescription());
        }
        String wxOpenid = user.getWxOpenId();
        String alipayId = user.getAlipayId();
        if (!StringUtils.isEmpty(wxOpenid)) {
            vo.setWechat(true);
        }
        if (!StringUtils.isEmpty(alipayId)) {
            vo.setAlipay(true);
        }
        // 只要未全部绑定, 就为true
        if (StringUtils.isEmpty(wxOpenid) || StringUtils.isEmpty(alipayId)) {
            vo.setShow(true);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResult bindOther(BindOtherDto dto) throws Exception {
        // 根据用户token获取用户id信息
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            log.error(RespCode.NO_TOKEN.getDescription());
            throw new CommonException(RespCode.NO_TOKEN.getDescription());
        }
        Claims claims = jwtUtils.parseJWT(token);
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        String uid = String.valueOf(payload.getId());
        Long userId = Long.valueOf(uid);

        // 查询用户
        User user = userService.findById(userId);
        if (user == null) {
            log.error(RespCode.USER_ACCOUNT_NOT_FOUND.getDescription());
            return HttpResult.fail(RespCode.USER_ACCOUNT_NOT_FOUND);
        }
        // 根据code获取用户的三方账户信息, 绑定三方(更新用户的跨店卡信息)
        Object userInfo = null;

        String code = dto.getCode();
        Long bindTime = System.currentTimeMillis() / 1000;
        if (Constant.WX == dto.getType()) {
            // 更新微信ID绑定时间 和 三方信息
            user.setWxBindTime(bindTime);
            AccessTokenVo vo = wxLoginService.getAccessToken(code);
            if (vo.getOpenId().equals(user.getWxOpenId())) {
                log.error(RespCode.WX_ALLREADY_BOUND.getDescription());
                return HttpResult.fail(RespCode.WX_ALLREADY_BOUND);
            }
            user.setWxOpenId(vo.getOpenId());
            // 获取用户个人微信信息
            vo.setBindTime(bindTime);
            userInfo = wxLoginService.getUserInfo(vo);
        }
        if (Constant.ALI == dto.getType()) {
            // 更新支付宝ID绑定时间 和 三方信息
            user.setAliBindTime(bindTime);
            AccessTokenVo vo = aliLoginService.getAuthToken(code);
            if (vo.getOpenId().equals(user.getAlipayId())) {
                log.error(RespCode.ALI_ALLREADY_BOUND.getDescription());
                return HttpResult.fail(RespCode.ALI_ALLREADY_BOUND);
            }
            user.setAlipayId(vo.getOpenId());
            userInfo = aliLoginService.getUserInfo(vo.getAccessToken());
        }
        userService.update(user);

        if (userInfo != null) {
            BeanUtils.copyProperties(user, userInfo);
            // 更新用户信息表
            userService.updateById(user);
            // 更新缓存
            redisUtil.del(Constant.USER_PREFIX + user.getId());
            String userStr = mapper.writeValueAsString(user);
            redisUtil.set(Constant.USER_PREFIX + user.getId(), userStr, Constant.LOGIN_EXPIRE_TIME);
            return HttpResult.success("三方绑定成功!");
        }
        return HttpResult.fail("三方绑定异常!");
    }

    @Override
    public ThirdLoginVo thirdLogin(ThirdLoginDto dto) throws Exception {
        // 根据type 和 code 调用三方接口认证, 获取openId
        AccessTokenVo vo = null;
        int type = dto.getType();
        if (Constant.WX == type) {
            vo = wxLoginService.getAccessToken(dto.getCode());
        }
        if (Constant.ALI == type) {
            vo = aliLoginService.getAuthToken(dto.getCode());
        }
        String openId = vo.getOpenId();
        // 根据openId查询用户信息(无用户说明未绑定)
        User user = find(openId, type);
        ThirdLoginVo thirdLoginVo = new ThirdLoginVo();
        thirdLoginVo.setOpenId(openId);
        if (user == null) {
            thirdLoginVo.setBindPhone(false);
            thirdLoginVo.setToken("");
            return thirdLoginVo;
        }
        if (user != null && !StringUtils.isEmpty(user.getMobile())) {
            // 查询用户是否已绑定手机
            thirdLoginVo.setBindPhone(true);
            String token = login(user);
            thirdLoginVo.setToken(token);
        }
        return thirdLoginVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysToken bindPhone(BindPhoneDto dto) throws Exception {
        // 参数校验: 查询用户的手机号是否已注册
        User user = userService.findByMobile(dto.getPhone());
        user = insert(user, dto);
        String token = login(user);
        SysToken sysToken = new SysToken(token);
        return sysToken;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OneKeyVo oneKey(OneKeyLoginTokenDto dto) throws Exception {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(Constant.JiGuang.VERIFY_URL);
        String authStringEnc = Base64.encodeToString(Constant.JiGuang.APP_KEY + ":" + Constant.JiGuang.MASTER_SECRET);
        postRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
        postRequest.setHeader("Authorization", "Basic " + authStringEnc);

        OneKeyVo vo = new OneKeyVo();
        JsonObject params = new JsonObject();
        params.addProperty("loginToken", dto.getOneKeyLoginToken());
        StringEntity requestEntity = new StringEntity(params.toString(), "UTF-8");
        postRequest.setEntity(requestEntity);
        // 发送请求
        CloseableHttpResponse response = httpclient.execute(postRequest);
        HttpEntity entity = response.getEntity();
        // 解析结果
        String entityStr = EntityUtils.toString(entity, "UTF-8");
        OneKeyRespVo oneKeyRespVo = mapper.readValue(entityStr, new TypeReference<OneKeyRespVo>() {
        });
        String code = oneKeyRespVo.getCode();
        String phoneContent = oneKeyRespVo.getPhone();
        if ("9003".equals(code)) {
            log.error(RespCode.JIGUANG_TOKEN_EXPIRED.getDescription());
            throw new CommonException(RespCode.JIGUANG_TOKEN_EXPIRED.getDescription());
        }
        // 判断状态码不为 8000 则验证失败
        if (!"8000".equals(code)) {
            log.error(RespCode.JIGUANG_CHECK_FAILED.getDescription());
            throw new CommonException(RespCode.JIGUANG_CHECK_FAILED.getDescription());
        }
        // 解析手机号
        String mobile = RSADecryptUtil.decrypt(phoneContent, Constant.JiGuang.PRIVATE_KEY);
        // 根据手机查找账户
        User user = find(mobile);
        // 无账户, 创建
        user = insert(user, mobile);
        // 查询用户是否已绑定第三方
        Boolean bindFlag = false;
        // 是否已绑定第三方: 只要绑定一个就为true
        if (!StringUtils.isEmpty(user.getWxOpenId()) || !StringUtils.isEmpty(user.getAlipayId())) {
            bindFlag = true;
        }
        // 登录, 生成登录token
        String token = login(user);
        vo.setToken(token);
        vo.setBindOther(bindFlag);
        return vo;
    }

    @Override
    public UserVo findUserInfo() throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new CommonException(RespCode.NO_TOKEN.getDescription());
        }
        Claims claims = jwtUtils.parseJWT(token);
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        String uid = String.valueOf(payload.getId());
        Long userId = Long.valueOf(uid);
        // 从缓存拿
        Object redisData = redisUtil.get(Constant.USER_PREFIX + userId);
        User user = null;
        if (redisData != null) {
            user = mapper.readValue(redisData.toString(), new TypeReference<User>() {
            });
        } else {
            // 从数据库拿, 放入缓存
            user = userService.findById(userId);
            String userStr = mapper.writeValueAsString(user);
            redisUtil.set(Constant.USER_PREFIX + userId, userStr, Constant.LOGIN_EXPIRE_TIME);
        }
        UserVo.UserDetail userDetail = new UserVo.UserDetail();
        userDetail.setUid(user.getId());
        userDetail.setToken(token);
        userDetail.setRealname(user.getUsername());
        userDetail.setNickname(user.getNickname());
        userDetail.setPhone(user.getMobile());
        userDetail.setAvatar(StringUtils.isEmpty(user.getHeadimgurl()) ? Constant.DEFAULT_AVATAR :
                user.getHeadimgurl());
        userDetail.setBirthday(user.getBirthday());
        userDetail.setRegdate(user.getCreateTime());
        userDetail.setStatus(user.getStatus());
        userDetail.setGender(user.getSex());
        UserVo vo = new UserVo();
        vo.setUser(userDetail);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditUserVo editUserInfo(EditUserDto dto) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new CommonException(RespCode.NO_TOKEN.getDescription());
        }
        Claims claims = jwtUtils.parseJWT(token);
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        String uid = String.valueOf(payload.getId());
        Long userId = Long.valueOf(uid);

        // 根据id查询用户信息
        User user = userService.findById(userId);
        // 更新用户信息
        user.setNickname(dto.getNickname());
        user.setBirthday(dto.getBirthday());
        user.setHeadimgurl(dto.getAvatar());
        user.setMobile(dto.getPhone());
        userService.updateById(user);
        // 更新缓存
        redisUtil.del(Constant.USER_PREFIX + userId);
        String userInfo = mapper.writeValueAsString(user);
        redisUtil.set(Constant.USER_PREFIX + userId, userInfo, Constant.LOGIN_EXPIRE_TIME);

        EditUserVo result = parseEditUserVo(userId, token);
        return result;
    }

    @Override
    public void update(User user) throws Exception {
        userService.updateById(user);
    }

    @Override
    public User find(String openId, int type) throws Exception {
        User user = null;
        if (Constant.WX == type) {
            user = userService.findWxAccount(openId);
        }
        if (Constant.ALI == type) {
            user = userService.findAliAccount(openId);
        }
        return user;
    }

    @Override
    public User find(String mobile) {
        return userService.findByMobile(mobile);
    }

    @Override
    public User findById(Long uid) throws Exception {
        return userService.findById(uid);
    }

    private EditUserVo parseEditUserVo(Long userId, String token) throws Exception {
        EditUserVo result = new EditUserVo();
        User user = userService.findById(userId);
        result.setUid(user.getId());
        result.setToken(token);
        result.setNickname(user.getNickname());
        result.setRealname(user.getUsername());
        //TODO 头像暂时用一个,年龄字段未加
        result.setAvatar(StringUtils.isEmpty(user.getHeadimgurl()) ? Constant.DEFAULT_AVATAR :
                user.getHeadimgurl());
        result.setAvatarLarge(user.getHeadimgurl());
        result.setAge("0");

        result.setGender(user.getSex());

        //TODO 是否需要格式化,个人描述字段未加
        result.setBirthday(user.getBirthday());
        result.setIntro("这家伙很懒，什么都没有写");
        result.setPhone(user.getMobile());
        result.setMail(user.getEmail());
        result.setRegdate(user.getCreateTime());
        if (user.getCreateTime() != null) {
            Date d = new Date(user.getCreateTime() * 1000);
            result.setRegdatestr(DateUtils.date2LocalDateStr(d, "yyyy年MM月dd日"));
        }
        result.setStatus(user.getStatus());
        return result;
    }

}
