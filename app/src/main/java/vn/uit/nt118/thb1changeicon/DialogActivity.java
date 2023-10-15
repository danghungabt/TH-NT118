package vn.uit.nt118.thb1changeicon;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Log.v("activity2", "Create");

        //Tạo đối tượng
        AlertDialog.Builder b = new AlertDialog.Builder(this);

        b.setTitle("Đến giờ");
        /*b.setMessage("Bạn có đồng ý thoát chương trình không?");*/

        b.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

//        b.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        });

        AlertDialog al = b.create();
        al.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("activity2", "Pause");

    }
}
