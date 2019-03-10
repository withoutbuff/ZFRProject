package com.example.zfrproject.fragmentUser;
import com.example.zfrproject.fragmentAdmin.AAdminFragment;
import com.example.zfrproject.fragmentAdmin.AFettlerFragment;
import com.example.zfrproject.fragmentAdmin.AMassageChairFragment;
import com.example.zfrproject.fragmentAdmin.AMassageChairLocationFragment;
import com.example.zfrproject.fragmentAdmin.AStatementFragment;
import com.example.zfrproject.fragmentAdmin.AUserFragment;
import java.util.HashMap;

public class FragmentFactory {
    private static HashMap<Integer, BaseFragment> mBaseFragments = new HashMap<Integer, BaseFragment>();

    public static BaseFragment createUserFragment(int pos) {
        BaseFragment baseFragment = mBaseFragments.get(pos);

        if (baseFragment == null) {
            switch (pos) {
                case 0:
                    baseFragment = new MapFragment();
                    break;
                case 1:
                    baseFragment = new NewFragment();
                    break;
                case 2:
                    baseFragment = new IntroductionFragment();
                    break;
                case 3:
                    baseFragment = new AccountFragment();
                    break;

            }
            mBaseFragments.put(pos, baseFragment);
        }
        return baseFragment;
    }

    public static BaseFragment createAdminFragment(int pos) {
        BaseFragment baseFragment = mBaseFragments.get(pos);

        if (baseFragment == null) {
            switch (pos) {
                case 0:
                    baseFragment = new AUserFragment();
                    break;
                case 1:
                    baseFragment = new AAdminFragment();
                    break;
                case 2:
                    baseFragment = new AFettlerFragment();
                    break;
                case 3:
                    baseFragment = new AMassageChairLocationFragment();
                    break;
                case 4:
                    baseFragment = new AMassageChairFragment();
                    break;
                case 5:
                    baseFragment = new AStatementFragment();
                    break;
            }
            mBaseFragments.put(pos, baseFragment);
        }
        return baseFragment;
    }
}
