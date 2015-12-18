package in.newzbyte.app;
 /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
import android.support.v4.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class Custom_IntroSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static Custom_IntroSlidePageFragment create(int pageNumber) {
        Custom_IntroSlidePageFragment fragment = new Custom_IntroSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Custom_IntroSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        

        return getViewForPage(inflater, container, mPageNumber);
    }

    private View getViewForPage(LayoutInflater inflater, ViewGroup container,int pageNo){
    	
    	View view = null;
    	
    	switch (pageNo) {
		case 0:
			view = (View) inflater
            .inflate(R.layout.view_intro_first, container, false);
			ImageView image = (ImageView) view.findViewById(R.id.imgLogo);
            //image.setBackgroundResource(R.drawable.anim_newzbyte_logo);
            ((AnimationDrawable) image.getBackground()).start();
			break;
		case 1:
			view = (View) inflater
            .inflate(R.layout.view_intro_second, container, false);
			break;
		case 2:
			view = (View) inflater
            .inflate(R.layout.view_intro_third, container, false);
			break;

		default:
			view = (View) inflater
            .inflate(R.layout.view_intro_first, container, false);
			break;
		}
    	
    	return view;
    }
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
    }