package com.irfan.draft1.Studies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfan.draft1.News.NewsLearnMore;
import com.irfan.draft1.R;

/**
 * Created by irfan on 07/11/2017.
 */

public class StudiesFragment extends Fragment implements View.OnClickListener {

    private CardView moodle, blackboard, oasis,estudent;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studies_fragment, container, false);

        moodle = view.findViewById(R.id.moodle);
        blackboard = view.findViewById(R.id.blackboard);
        oasis = view.findViewById(R.id.oasis);
        estudent = view.findViewById(R.id.estudent);

        moodle.setOnClickListener(this);
        blackboard.setOnClickListener(this);
        oasis.setOnClickListener(this);
        estudent.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.moodle:
                openNews("http://moodle.curtin.edu.my/");

                break;
            case R.id.blackboard:
                openNews("https://lms.curtin.edu.au");

                break;
            case R.id.oasis:
                openNews("https://oasis.curtin.edu.au/Auth/LogOn");
                break;
            case R.id.estudent:
                openNews("http://estudent.curtin.edu.my/eStudentProd/login.aspx?ReturnUrl=%2feStudentProd%2fdefault.aspx");
                break;
        }
    }

    private void openNews(String url) {
        Intent intent = new Intent(getActivity(), StudiesDetailActivity.class);
        intent.putExtra("URL", url);
//            context.startActivity(intent);
        //Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(eventNews,eventNews.getLeft(),eventNews.getTop(),eventNews.getWidth(),eventNews.getHeight()).toBundle();
        getActivity().startActivity(intent);

    }
}
