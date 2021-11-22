package com.projectflow.projectflow.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FcmFacadeImpl implements FcmFacade {

    private static final String path = "google-services.json";

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(path).getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
                System.out.println("Initialized!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFcmMessageOnSocket(User sender, List<User> users, String title, String content, MessageType type, String profileImage) {
        users.remove(sender);
        var fcm = MulticastMessage.builder()
                .setAndroidConfig(AndroidConfig.builder()
                        .putData("title", title)
                        .putData("body", content)
                        .putData("click_action", type.toString())
                        .putData("image", profileImage)
                        .build())
                .addAllTokens(users.stream().map(User::getDeviceToken).collect(Collectors.toList()))
                .build();
        FirebaseMessaging.getInstance().sendMulticastAsync(fcm);
    }
}
