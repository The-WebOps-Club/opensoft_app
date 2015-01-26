package opensoft.com.opensoft;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import opensoft.browse.AdapterCardElement;
import opensoft.browse.CardElement;
import opensoft.search.AdapterListElement;
import opensoft.search.ListElement;
import opensoft.util.SwipeableRecyclerViewTouchListener;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BrowseFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SearchFragment extends Fragment {
        AdapterListElement adapter;
        public SearchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.listmain);
            recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

            List<ListElement> data= new ArrayList<ListElement>();
            data.add(new ListElement("first","caption"));
            data.add(new ListElement("second"));

            final AdapterListElement adapterListElement = new AdapterListElement(data,rootView.getContext());
            recyclerView.setAdapter(adapterListElement);
            this.adapter = adapterListElement;
            EditText searchbar=(EditText) rootView.findViewById(R.id.search_bar);
            searchbar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    ;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.filter(s, rootView.getContext());
                    //adapterListElement.notifyDataSetChanged();
                    recyclerView.swapAdapter(adapter, false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    ;
                }
            });
            return rootView;
        }
    }

    public static class BrowseFragment extends Fragment {
        AdapterListElement adapter;
        public BrowseFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_browse, container, false);
            final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.card_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            List<CardElement> list=new ArrayList<>();
            list.add(new CardElement("first","content"));
            list.add(new CardElement("second","no content"));
            final AdapterCardElement adapter=new AdapterCardElement(list);
            recyclerView.setAdapter(adapter);
            SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(recyclerView,
                            new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                @Override
                                public boolean canSwipe(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        adapter.removeFromList(position);
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        adapter.removeFromList(position);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });
            recyclerView.addOnItemTouchListener(swipeTouchListener);
            return rootView;
        }
    }
}
