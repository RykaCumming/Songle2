//package uk.ac.ed.inf.songle2;
//
//import android.app.DialogFragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by s1540547 on 07/11/17.
// */
//
//
//public class WordListFragment extends DialogFragment {
//    public static WordListFragment newInstance(ArrayList<String> inp){
//        Bundle args = new Bundle();
////        args.putStringArrayList("num",);
////        WordListFragment.setArguments(args);
//        ListView listView2;
//        ArrayAdapter adapter;
////        adapter = new ArrayAdapter(ScrollingActivity.this,android.R.layout.simple_list_item_1,list);
////        listView2.setAdapter(adapter);
//
//
//        WordListFragment wordListFragment = new WordListFragment();
//        return wordListFragment;
//    }
//
///*    public static EasyFragment newInstance(String num){
//        EasyFragment easyFragment = new EasyFragment();
//       return easyFragment;
//    }*/
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
////        num = bundle.getString("num");
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
////        ListView listView2 = findViewById(R.id.list_view_fragm);
//        return view;
//    }
//}
//


package uk.ac.ed.inf.songle2;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;


public class WordListFragment extends DialogFragment {
//    private ArrayList<String> wordlistinfragment;

    ListView listView2;
    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_list, container,
                false);
        ArrayList<String> thewordlist = getArguments().getStringArrayList("wordlist");
        listView2 = rootView.findViewById(R.id.list_view_fragm);listView2.setAdapter(adapter);
        adapter=new ArrayAdapter(rootView.getContext(),android.R.layout.simple_list_item_1,thewordlist);
        listView2.setAdapter(adapter);
        getDialog().setTitle("Word List");
        return rootView;
    }

    public static WordListFragment newInstance() {
        WordListFragment frag = new WordListFragment();
//        Bundle args = new Bundle();
//'        wordlistinfragment=
 //       args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
    }

}