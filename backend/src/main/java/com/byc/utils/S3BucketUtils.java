package com.byc.utils;

import java.io.File;
import com.byc.common.exception.BusinessException;
import com.byc.common.model.ErrorCode;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class S3BucketUtils {

    // key name: s3
    private static final String ACCESS_KEY = "AKIAW6K3DIQMMHULKSON";
    private static final String SECRET_KEY = "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";

    private static final String BUCKET_NAME = "buapi";
    private static final Region REGION = Region.US_EAST_1;

    private static S3Client s3Client = S3Client.builder()
            .region(REGION)  // 替换成你的 S3 存储桶所在的 AWS 区域
            .credentialsProvider(
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY))
            )
            .build();


    public static String uploadFileToS3(String bucketKey, File file) {
        try {
            PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(bucketKey)
                    .build(), file.toPath());

            System.out.println("Upload successful. ETag: " + response.eTag());
            return response.eTag();
        } catch (Exception e) {
            System.err.println("Error uploading file to S3: " + e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }
}
