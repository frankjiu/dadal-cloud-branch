package com.core.config.serializer;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.Deflater;

public class GzipRedisSerializer<T> implements RedisSerializer<T> {

    private static final int BUFFER_SIZE = 4096;
    private RedisSerializer<T> innerSerializer;
    private int level = Deflater.BEST_SPEED;

    public GzipRedisSerializer(RedisSerializer<T> innerSerializer) {
        this.innerSerializer = innerSerializer;
    }

    public GzipRedisSerializer(RedisSerializer<T> innerSerializer,int level) {
        this.level = level;
        this.innerSerializer = innerSerializer;
    }

    @Override
    public byte[] serialize(T graph) throws SerializationException {

        Assert.notNull(graph,"now allow null value!");

        GzipParameters param = new GzipParameters();
        param.setCompressionLevel(level);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GzipCompressorOutputStream os = new GzipCompressorOutputStream(baos, param);){
            byte[] bytes = innerSerializer.serialize(graph);
            os.write(Objects.requireNonNull(bytes));
            os.finish();
            return bytes;
        }catch (IOException e){
            throw new SerializationException("Gzip Serialization Error", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {

        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            GzipCompressorInputStream is = new GzipCompressorInputStream(bais);){
            byte[] datas = IOUtils.toByteArray(is);
            return innerSerializer.deserialize(datas);
        }catch (IOException e){
            throw new SerializationException("Gzip Serialization Error", e);
        }
    }
}