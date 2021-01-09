package com.core.config.serializer;

import lombok.Cleanup;
import net.jpountz.lz4.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Lz4RedisSerializer<T> implements RedisSerializer<T> {

    private static final int BUFFER_SIZE = 1 << 20;// 1M

    private RedisSerializer<T> innerSerializer;
    private LZ4FastDecompressor decompresser;
    private LZ4Compressor compressor;

    public Lz4RedisSerializer(RedisSerializer<T> innerSerializer) {
        this.innerSerializer = innerSerializer;
        LZ4Factory factory = LZ4Factory.fastestInstance();
        this.compressor = factory.fastCompressor();
        this.decompresser = factory.fastDecompressor();
    }

    @Override
    public byte[] serialize(T graph){
        if (graph == null) {
            return new byte[0];
        }
        try {
            byte[] bytes = innerSerializer.serialize(graph);
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            @Cleanup LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(byteOutput, BUFFER_SIZE, compressor);
            compressedOutput.write(bytes);
            compressedOutput.finish();
            return byteOutput.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("LZ4 Serialization Error", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes){

        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
            @Cleanup LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(bytes), decompresser);
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((count = lzis.read(buffer)) != -1) {
                baos.write(buffer, 0, count);
            }
            T result = innerSerializer.deserialize(baos.toByteArray());
            return result;
        } catch (Exception e) {
            throw new SerializationException("LZ4 deserizelie error", e);
        }
    }
}