package ahmed.yacoubi.e_commerce.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.model.IntroItem;


public class IntroFragment extends Fragment {


    private String text;
    private String text2;
    private int image;
    private static final String IMAGE = "img";
    private static final String TEXT = "text";
    private static final String TEXT2 = "text2";

    public IntroFragment() {
        // Required empty public constructor
    }


    public static IntroFragment newInstance(IntroItem item) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, item.getText());
        args.putString(TEXT2, item.getText2());
        args.putInt(IMAGE, item.getImage());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
            text2 = getArguments().getString(TEXT2);
            image = getArguments().getInt(IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_intro, container, false);
        ImageView img = v.findViewById(R.id.fragment_intro_img);
        TextView tv_text = v.findViewById(R.id.fragment_intro_tv_text);
        TextView tv_text2 = v.findViewById(R.id.fragment_intro_tv_text2);
        img.setImageResource(this.image);
        tv_text.setText(this.text);
        tv_text2.setText(this.text2);
        return v;
    }
}