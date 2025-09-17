package com.slice;

import com.slice.entity.User;
import com.slice.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
class SliceApplicationTests {


    @Resource
    UserService userService;

    @Test
    void insertUsersAsync() {
        int totalUsers = 100000;
        int batchSize = 10000;
        int numThreads = totalUsers / batchSize;
        long phonePrefix = 13800000000L;

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                List<User> users = new ArrayList<>(batchSize);
                long startPhone = phonePrefix + (long) threadIndex * batchSize;

                for (int j = 0; j < batchSize; j++) {
                    User user = new User();
                    user.setPassword("b0fb253c3cde6e2cc5ac8d524c9ff333");
                    user.setPhone(String.valueOf(startPhone + j));
                    users.add(user);
                }

                System.out.println("Thread-" + threadIndex + " starting to insert batch of " + users.size() + " users.");
                userService.saveBatch(users);
                System.out.println("Thread-" + threadIndex + " finished inserting.");
            });
            futures.add(future);
        }

        // 等待所有异步任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println("所有用户插入完成！");
    }

}
