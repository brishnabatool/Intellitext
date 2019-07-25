package com.smd.project.ahmad.intellignizer;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;

    public static final String PREF_FILE_NAME="testPref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    private recyclerViewAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawLayout;
    private View containerView;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    public boolean isDrawerOpened=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getContext(), KEY_USER_LEARNED_DRAWER, "false")) ;

        if(savedInstanceState!=null)
        {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView= (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new recyclerViewAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        SpacesItemDecorations decoration = new SpacesItemDecorations(1);
        recyclerView.addItemDecoration(decoration);
        return layout;
    }



    public static List<navigationElement> getData()
    {
        List<navigationElement> data = new ArrayList<>();

        String[] titles = {"About Application"};
        int[] icons={R.drawable.about};

        for( int i=0; i<titles.length && i<icons.length; i++)
        {
            navigationElement current=new navigationElement();
            current.icon=icons[i];
            current.title=titles[i];
            data.add(current);
        }

        return data;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final android.support.v7.widget.Toolbar toolbar)
    {
        containerView= getActivity().findViewById(fragmentId);
        mDrawLayout=drawerLayout;
        mDrawerToggle=new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                if(slideOffset<0.6)
                {
                    toolbar.setAlpha(1-slideOffset);
                }

            }
        };

        if(!mUserLearnedDrawer && mFromSavedInstanceState)
        {
            mDrawLayout.openDrawer(containerView);
        }

        mDrawLayout.setDrawerListener(mDrawerToggle);
        mDrawLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }


    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

}
