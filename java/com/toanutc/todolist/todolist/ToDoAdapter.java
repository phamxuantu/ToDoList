package com.toanutc.todolist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private ArrayList<ToDo> toDos;
    Context context;
    private int layout;
    SQLiteHelper db;

    ToDoAdapter(ArrayList<ToDo> toDos, Context context, int layout, SQLiteHelper db) {
        this.toDos = toDos;
        this.context = context;
        this.layout = layout;
        this.db = db;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(layout, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (MainActivity.TAB_POSITION == 1 || MainActivity.TAB_POSITION == 2) {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        holder.checkBox.setChecked(toDos.get(position).getCheck());
        holder.txtvName.setText(toDos.get(position).getName());
        holder.linearLayoutItem.setTag(position);
        if (toDos.get(position).getCheck()) {
            holder.txtvName.setPaintFlags(holder.txtvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.txtvDetail.setText(toDos.get(position).getCategories() +
                (toDos.get(position).getTime().equals("") ? "" : " | " + toDos.get(position).getTime()) +
                (toDos.get(position).getDescription().equals("") ? "" : " | " + toDos.get(position).getDescription())
        );
        holder.imageView.setImageResource(toDos.get(position).getImageAlert());

        if (toDos.get(position).getCheck()) {
            holder.checkBox.setEnabled(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txtvName.setPaintFlags(holder.txtvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Util.stopJob(toDos.get(position).getId(), context);
                db.updateTodo(new ToDo(toDos.get(position).getId(), true, toDos.get(position).getName(), toDos.get(position).getTime(), toDos.get(position).getCategories(), toDos.get(position).getDescription(), toDos.get(position).getImageAlert()));
                holder.checkBox.setEnabled(false);
            }
        });

        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông tin công việc");
                int position = (int) holder.linearLayoutItem.getTag();
                final int[] selectedSpinner = {0};

                final ToDo toDo = db.getToDo(toDos.get(position).getId());

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                @SuppressLint("InflateParams") View itemView = inflater.inflate(R.layout.layout_dialog, null);

                final EditText txtName = itemView.findViewById(R.id.txtNameToDo);
                txtName.setText(toDo.getName());

                for (int i = 0; i < MainActivity.arrCategory.length; i++) {
                    if (toDo.getCategories().equals(MainActivity.arrCategory[i])) {
                        selectedSpinner[0] = i;
                        break;
                    }
                }

                Spinner spinner = itemView.findViewById(R.id.spinnerCategory);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.context, android.R.layout.simple_spinner_item, MainActivity.arrCategory);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(adapter);
                spinner.setSelection(selectedSpinner[0]);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSpinner[0] = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                final EditText txtDesciption = itemView.findViewById(R.id.txtDescriptionToDo);
                txtDesciption.setText(toDo.getDescription());

                final TextView txtStatus = itemView.findViewById(R.id.txtStatusToDo);
                txtStatus.setText(toDo.getCheck() ? "Đã xong" : "Chưa xong");

                builder.setView(itemView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDo toDoUpdate = new ToDo(toDo.getId(), toDo.getCheck(), txtName.getText().toString(), toDo.getTime(), MainActivity.arrCategory[selectedSpinner[0]], txtDesciption.getText().toString(), toDo.getImageAlert());
                        db.updateTodo(toDoUpdate);
                        if (MainActivity.TAB_POSITION == 0) {
                            TabAll tabAll = new TabAll();
                            tabAll.updateList();
                        } else if (MainActivity.TAB_POSITION == 1) {
                            TabDone tabDone = new TabDone();
                            tabDone.updateList();
                        } else {
                            TabNotDone tabNotDone = new TabNotDone();
                            tabNotDone.updateList();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();


            }
        });

        holder.linearLayoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position = (int) holder.linearLayoutItem.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn xóa công việc này?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteTodo(toDos.get(position).getId());
                        if (MainActivity.TAB_POSITION == 0) {
                            TabAll tabAll = new TabAll();
                            tabAll.updateList();
                        } else if (MainActivity.TAB_POSITION == 1) {
                            TabDone tabDone = new TabDone();
                            tabDone.updateList();
                        } else {
                            TabNotDone tabNotDone = new TabNotDone();
                            tabNotDone.updateList();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView txtvName, txtvDetail;
        ImageView imageView;
        LinearLayout linearLayoutItem;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkTodo);
            txtvName = itemView.findViewById(R.id.txtName);
            txtvDetail = itemView.findViewById(R.id.txtDetail);
            imageView = itemView.findViewById(R.id.imgAlarm);
            linearLayoutItem = itemView.findViewById(R.id.ll_item);
        }

    }

}
