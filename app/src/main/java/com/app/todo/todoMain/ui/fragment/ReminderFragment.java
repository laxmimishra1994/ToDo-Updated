package com.app.todo.todoMain.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.todo.R;
import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todoMain.presenter.ReminderFragmentPresenter;
import com.app.todo.todoMain.presenter.ReminderFragmentPresenterInterface;
import com.app.todo.todoMain.ui.activity.TodoMainActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderFragment extends Fragment implements ReminderFragmentInterface {
    public static final String TAG = "NotesFragment";
    TodoMainActivity todoMainActivity;
    ReminderFragmentPresenterInterface presenter;
    FirebaseAuth firebaseAuth;
    RecyclerAdapter reminder_adapter;
    RecyclerView mrecyclerView;
    ProgressDialog progressDialog;
    AppCompatTextView reminderTextView;
    AppCompatImageView reminderImageView;
    LinearLayout linearLayout;
    ArrayList<NotesModel> notesModelArrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        initView(view);
        getActivity().setTitle("Reminder");
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        presenter.getReminderNotes(uId);
        return view;
    }

    private void initView(View view) {
        linearLayout= (LinearLayout) view.findViewById(R.id.reminder_rootLayout);
        mrecyclerView = (RecyclerView) view.findViewById(R.id.reminder_recyclerView);
        reminderTextView= (AppCompatTextView) view.findViewById(R.id.reminder_textView);
        reminderImageView= (AppCompatImageView) view.findViewById(R.id.reminder_event_icon);
        firebaseAuth = FirebaseAuth.getInstance();
        mrecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((TodoMainActivity) getActivity()).showOrHideFab(false);
    }

    public ReminderFragment(TodoMainActivity todoMainActivity) {
        this.todoMainActivity = todoMainActivity;
        presenter = new ReminderFragmentPresenter(todoMainActivity, this);
    }

    @Override
    public void showDialog(String message) {
        progressDialog=new ProgressDialog(todoMainActivity);
        if (!todoMainActivity.isFinishing()){
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if(!todoMainActivity.isFinishing() && progressDialog !=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void gettingReminderSuccess(List<NotesModel> notesModelList) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_time));
        String currentDate = format.format(date.getTime());
        ArrayList<NotesModel> reminderNoteList=new ArrayList<>();
        for (NotesModel notesModel: notesModelList){
            if (notesModel.getReminderDate().equals(currentDate) && !(notesModel.isArchieved())){
                reminderNoteList.add(notesModel);
            }
        }
        reminder_adapter= new RecyclerAdapter(todoMainActivity,reminderNoteList, this);
        mrecyclerView.setAdapter(reminder_adapter);

        if(reminderNoteList.size()!=0){
            reminderTextView.setVisibility(View.INVISIBLE);
            reminderImageView.setVisibility(View.INVISIBLE);
            linearLayout.setGravity(Gravity.START);
        }else {
            reminderTextView.setVisibility(View.VISIBLE);
            reminderImageView.setVisibility(View.VISIBLE);
            linearLayout.setGravity(Gravity.CENTER);

        }
    }

    @Override
    public void gettingReminderFailure(String message) {

    }

}
