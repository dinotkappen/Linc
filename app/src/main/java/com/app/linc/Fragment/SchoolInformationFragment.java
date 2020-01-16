package com.app.linc.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.linc.Adapter.NoticeBoardRecyclerviewAdapter;
import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.Model.StudentHomeModel.SchoolInformation;
import com.app.linc.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import static com.app.linc.Activities.MainActivity.titlHide;


public class SchoolInformationFragment extends Fragment {
    ImageView imgSchoolLogo;
    String url;
    TextView txtAdrz, txtEmail, txtPhone, txtTitle,txtContent;
    List<SchoolInformation> schoolInformationList = new ArrayList<>();
    private AVLoadingIndicatorView aviImgLoader;
    public SchoolInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_school_information, container, false);
        Hawk.put("currentFragment",false);
        imgSchoolLogo = rootView.findViewById(R.id.imgSchoolLogo);
        titlHide(getResources().getString(R.string.school_information));
        txtTitle = rootView.findViewById(R.id.txtTitle);
        txtAdrz = rootView.findViewById(R.id.txtAdrz);
        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtPhone = rootView.findViewById(R.id.txtPhone);
        txtContent= rootView.findViewById(R.id.txtContent);
        aviImgLoader=rootView.findViewById(R.id.aviSmall);
        aviImgLoader.show();
        schoolInformationList = Hawk.get("schoolInformationList", schoolInformationList);
        if (schoolInformationList.size() > 0) {
            for (int i = 0; i < schoolInformationList.size(); i++) {
                url = schoolInformationList.get(i).getPhoto();
                if (url != null && !url.isEmpty() && !url.equals("null")) {
                    url = getString(R.string.imgBasrUrl) + url;
                    String title = schoolInformationList.get(i).getName();
                    String phone = schoolInformationList.get(i).getPhone();
                    String email = schoolInformationList.get(i).getEmail();
                    String adrz = schoolInformationList.get(i).getAddress();


                    Glide.with(getActivity())
                            .load(url)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    aviImgLoader.hide();
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    aviImgLoader.hide();
                                    return false;
                                }
                            })
                            .apply(RequestOptions.circleCropTransform())
                            .into(imgSchoolLogo)
                    ;
//                    Glide.with(this)
//                            .load(url)
//                            .placeholder(circularProgressDrawable)
//                            .apply(RequestOptions.circleCropTransform())
//                            .into(imgSchoolLogo);

                    if (title != null && !title.isEmpty() && !title.equals("null")) {
                        txtTitle.setText(title);
                    }
                    if (phone != null && !phone.isEmpty() && !phone.equals("null")) {
                        txtPhone.setText(phone);
                    }
                    if (email != null && !email.isEmpty() && !email.equals("null")) {
                        txtEmail.setText(email);
                    }
                    if (adrz != null && !adrz.isEmpty() && !adrz.equals("null")) {
                        txtAdrz.setText(adrz);
                    }
                } else {
                    url = getString(R.string.imgBasrUrl) + "students/no.png";
                    Glide.with(this)
                            .load(url)
                            .apply(RequestOptions.circleCropTransform())
                            .into(imgSchoolLogo);
                    aviImgLoader.hide();
                }
            }
        }


        return rootView;
    }


}
