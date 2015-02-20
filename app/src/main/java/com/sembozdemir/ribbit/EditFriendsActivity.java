package com.sembozdemir.ribbit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class EditFriendsActivity extends ActionBarActivity {

    private static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);

        listView = (ListView) findViewById(R.id.edit_friends_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // listedeki elemanları coklu secmeye yarar

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    // Success
                    mUsers = parseUsers;
                    String[] usernames = new String[mUsers.size()];
                    int i = 0;
                    for(ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);
                    listView.setAdapter(adapter);

                    addFriendCheckmarks();

                } else {
                    Log.e(TAG, e.getMessage());
                    // There is a ParseException !!
                    // Dialogla exceptionın ne olduğunu gosterelim.
                    AlertDialog.Builder errDialogbuilder = new AlertDialog.Builder(EditFriendsActivity.this);
                    errDialogbuilder.setMessage(e.getMessage()); // Exception mesajı dialog'da gosterilecek.
                    errDialogbuilder.setTitle(R.string.err_title);
                    errDialogbuilder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog errDialog = errDialogbuilder.create();
                    errDialog.show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listView.isItemChecked(position)) {
                    // add friend
                    mFriendsRelation.add(mUsers.get(position));
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                } else {
                    // remove friend
                }
            }
        });
    }

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    // List returned - look for a match
                    for(int i = 0 ; i < mUsers.size() ; i++) {
                        ParseUser user = mUsers.get(i);

                        for(ParseUser friend : friends) {
                            if (user.getObjectId().equals(friend.getObjectId())) {
                                listView.setItemChecked(i, true);
                            }
                        }
                    }

                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
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
}
