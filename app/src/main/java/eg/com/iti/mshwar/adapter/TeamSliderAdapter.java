package eg.com.iti.mshwar.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.beans.TeamMember;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;

public class TeamSliderAdapter extends PagerAdapter {

    private static final String TAG = "TeamSliderAdapter";
    private Context context;
    private List<TeamMember> mList;

    public TeamSliderAdapter(Context context, List<TeamMember> mList){
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, container, false);

        TextView name = view.findViewById(R.id.txt_name_slider);
        ImageView image = view.findViewById(R.id.img_user_slider);
        ImageButton btnLinkedIn = view.findViewById(R.id.btn_linkedin);
        ImageButton btnGithub = view.findViewById(R.id.btn_github);
        ImageButton btnEmail = view.findViewById(R.id.btn_email);

        name.setText(mList.get(position).getName());
        image.setImageResource(mList.get(position).getImage());
        if(mList.get(position).getUrlLinkedIn() == null){
            btnLinkedIn.setVisibility(INVISIBLE);
        }

        if(mList.get(position).getUrlGitHub() == null){
            btnGithub.setVisibility(INVISIBLE);
        }

        if(mList.get(position).getEmail() == null){
            btnEmail.setVisibility(INVISIBLE);
        }

        btnLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri1 = Uri.parse(mList.get(position).getUrlLinkedIn()); // missing 'http://' will cause crashed
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                context.startActivity(intent1);
            }
        });

        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri2 = Uri.parse(mList.get(position).getUrlGitHub()); // missing 'http://' will cause crashed
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                context.startActivity(intent2);
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(android.content.Intent.ACTION_SEND);
                intent3.setType("plain/text");
                intent3.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mList.get(position).getEmail()});
                intent3.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mshwar App: ");
                context.startActivity(intent3);
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
