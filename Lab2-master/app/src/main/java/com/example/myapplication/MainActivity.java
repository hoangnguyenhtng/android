package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Contact> listContact;
    MyAdapter contactAdapter;
    ListView listViewContact;

    Button btnXoa,btnThem,btnSua;

    private int selectItemId;
    private Contact c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listContact = new ArrayList<>();
        listContact.add(new Contact(1,"Mot","123","https://png.pngtree.com/png-clipart/20190920/original/pngtree-user-flat-character-avatar-png-png-image_4650324.jpg",true));
        listContact.add(new Contact(2,"Hai","456","https://png.pngtree.com/png-clipart/20190920/original/pngtree-user-flat-character-avatar-png-png-image_4651285.jpg",false));
        listContact.add(new Contact(3,"Ba","789","https://png.pngtree.com/png-clipart/20190920/original/pngtree-user-flat-character-avatar-png-png-image_4654505.jpg",false));

        contactAdapter = new MyAdapter(listContact,this);

        listViewContact = findViewById(R.id.listview);
        listViewContact.setAdapter(contactAdapter);

        //Xoá khi tích vào checkbox
        btnXoa = findViewById(R.id.btnXoa);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xoá");
                builder.setMessage("Bạn có chắc muốn xoá không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<listContact.size();){
                            if(listContact.get(i).isStatus()==true){
                                listContact.remove(i);
                            }
                            else i++;
                        }
                        contactAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", null);
                builder.create().show();
            }
        });

        btnSua = findViewById(R.id.btnSua);
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean anyItemSelected = false;
                for(int i=0;i<listContact.size();){
                    if(listContact.get(i).isStatus()==true){
                        anyItemSelected = true;
                        selectItemId = i;
                        break;
                    }else {
                        i++;
                    }
                }
                if(anyItemSelected){ // Nếu có mục được chọn
                    Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                    c= listContact.get(selectItemId);
                    Bundle b = new Bundle();
                    b.putInt("id", c.getId());
                    b.putString("Image",c.getImage());
                    b.putString("name", c.getName());
                    b.putString("Phone",c.getPhoneNumber());
                    intent.putExtras(b);
                    startActivityForResult(intent,300);
                } else { // Nếu không có mục nào được chọn
                    Toast.makeText(MainActivity.this, "Vui lòng chọn một mục để sửa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Thêm mới
        btnThem = findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddView.class);
                startActivityForResult(intent,100);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b = data.getExtras();
        int id = b.getInt("Id");
        String name = b.getString("Name");
        String phone = b.getString("Phone");
        String image = b.getString("Image");
        Contact newcontact = new Contact(id,name,phone,image,false);
        if(requestCode == 100 && resultCode ==150){
            //truong hop them
            listContact.add(newcontact);
            contactAdapter.notifyDataSetChanged();
        }
        if(requestCode == 300 && resultCode == 120){
            Bundle bu =data.getExtras();
            int id_up = bu.getInt("Update_Id");
            String name_up = bu.getString("Update_Name");
            String phone_up = bu.getString("Update_Phone");
            String img= bu.getString("Update_Image");
            c.setId(id_up);
            c.setName(name_up);
            c.setPhoneNumber(phone_up);
            c.setImage(img);
            contactAdapter.notifyDataSetChanged();
        }

    }

}