package com.irfan.draft1.Notification;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by irfan on 11/01/2018.
 */

public class EventNotificationID extends FirebaseMessagingService //FirebaseInstanceIdService
{
//    private FirebaseFirestore db;
//    private FirebaseUser user;
//
////    @Override
////    public void onNewToken(String s) {
////        super.onNewToken(s);
////    }
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the InstanceID token
//     * is initially generated so this is where you would retrieve the token.
//     */
//    // [START refresh_token]
//
//
//
//
//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken);
//    }
//    // [END refresh_token]
//
//    /**
//     * Persist token to third-party servers.
//     *
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    public void sendRegistrationToServer(String token) {
//        db = FirebaseFirestore.getInstance();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user!=null) {
//            if (!user.isAnonymous()) {
//                db.collection("UserDatabase").document(user.getUid()).update("userToken", token).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                    }
//                });
//            }
//        }
//
//    }

}