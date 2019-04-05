package com.irfan.draft1.Maps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.irfan.draft1.R;
import com.irfan.draft1.Utilities.FeedbackModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FeedbackFragment extends Fragment implements View.OnClickListener {

    private ImageView backButton;
    private ImageView helpButton;
    private Button submitButton;
    private EditText reviewComment;
    private TextView reviewDate;

    private ProgressBar progressBar;

    private FirebaseUser user;
    private FirebaseFirestore db;

    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm a");


    private String collectionID;
    private int reviewCount;
    //private OnUpdateReview listener;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.feedback_fragment, container, false);
        backButton = view.findViewById(R.id.review_back);
        submitButton = view.findViewById(R.id.submit_review);
        helpButton = view.findViewById(R.id.review_help);
        reviewDate = view.findViewById(R.id.review_date);
        reviewComment = view.findViewById(R.id.review_comment);
        progressBar = view.findViewById(R.id.progress_bar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();


        reviewDate.setText("Date: "+sdf.format(new Date()));
//        if (previousReview != null) {
//            setPreviousReview(previousReview);
//        } else {
//            reviewCount++;
//            reviewComment.setHint("Tell us what you think of " + foodTruckName + "?");
//        }


        backButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);

//        db.collection("UserDatabase").document(user.getUid()).get().addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    if (task.getResult().getString("user_image")!=null) {
//                        userProfilePicture = task.getResult().getString("user_image");
//                    }
//                }
//            }
//        });
//

        return view;
    }


//    private void setPreviousReview(FoodTruckReviews previousReview) {
//        if (!previousReview.getfeedback_comment().equals("") ) {
//            reviewDate.setText(previousReview.getfeedback_title());
//            reviewComment.setText(previousReview.getfeedback_comment());
//        } else {
//            reviewComment.setHint("Tell us what you think of " + foodTruckName + "?");
//        }
//        ratingBar.setProgress(previousReview.getfeedback_rating());
//
//
//    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.review_back:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
            case R.id.submit_review:
                String comment = reviewComment.getText().toString();

//                Toast.makeText(getContext(),title+comment,Toast.LENGTH_LONG).show();
                if (validateFields(comment)) {
                    view.setClickable(false);
                    progressBar.setVisibility(View.VISIBLE);
                    addReview(comment, view);
                }
                break;
//            case R.id.ft_wfeedback_help:
//                ReviewHelpDialog helpDialog = new ReviewHelpDialog(getActivity());
//                helpDialog.show();
//                break;
        }

    }


    private void addReview( String comment, final View view) {

        if (user != null) {
            Map<String, Object> feedbackObject = new HashMap<>();

            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            feedbackObject.put("feedback_uid",uid);
            feedbackObject.put("feedback_user", name);
            feedbackObject.put("feedback_email", email);
            feedbackObject.put("feedback_date",sdf.format(new Date()));
            feedbackObject.put("feedback_comment", comment);

            db.collection("BusTrackingFeedback").add(feedbackObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    progressBar.setVisibility(View.GONE);

                    //Snackbar.make(view, "Thank you for your feedback. Much love!", Snackbar.LENGTH_LONG).setAction("OK", null).show();

                    getActivity().onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            listener = (OnUpdateReview) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Error in retrieving data. Please try again");
//        }
//    }

    private boolean validateFields(String feedback) {
        boolean valid = true;

        if (feedback.equals("")) {
            valid = false;
            Snackbar.make(view, "Please do not leave your feedback empty", Snackbar.LENGTH_LONG).setAction("OK", null).show();
        }


        return valid;
    }

//    public interface OnUpdateReview {
//        void updateReview(FeedbackModel updatedFeedback);
//    }
}

