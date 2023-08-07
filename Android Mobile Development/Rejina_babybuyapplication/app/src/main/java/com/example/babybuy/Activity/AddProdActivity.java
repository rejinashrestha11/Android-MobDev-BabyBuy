package com.example.babybuy.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babybuy.Database.BabyBuyDb;
import com.example.babybuy.Model.ProductDataModel;
import com.example.babybuy.R;

import java.io.ByteArrayOutputStream;

public class AddProdActivity extends AppCompatActivity {

    TextView productaddcamera, productaddgallery;
    Button productaddbtn;
    ImageView productaddimage, backicon;
    EditText productaddname, productadddes, productaddquantity, productaddprice;
    final int CAMERA_CODE = 100;
    final int GALLERY_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        //This is the code fot connecting with the database
        BabyBuyDb db = new BabyBuyDb(this);

        productaddbtn = findViewById(R.id.productaddbtnid);
        productaddname = findViewById(R.id.productaddtitleid);
        productaddquantity = findViewById(R.id.productaddquantityid);
        productaddprice = findViewById(R.id.productaddpriceid);
        productadddes = findViewById(R.id.productadddesid);
        productaddimage = findViewById(R.id.prodaddimgid);
        productaddgallery = findViewById(R.id.productaddfromgallery);
        productaddcamera = findViewById(R.id.productaddfromcamera);
        backicon = findViewById(R.id.backarrow);

        //Value of category id from Intent data pass
        int procatidlist = getIntent().getExtras().getInt("pcid");


        //From the gallery, add product image
        productaddgallery.setOnClickListener(v -> {
            Intent igallery = new Intent(Intent.ACTION_PICK);
            igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(igallery, GALLERY_CODE);
        });

        // From camera, add product image
        productaddcamera.setOnClickListener(v -> {
            Intent icamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(icamera, CAMERA_CODE);
        });

        //Adding the product data to product table of database
        productaddbtn.setOnClickListener(v -> {

            Integer prdstatus = -1;
            ProductDataModel productDataModel = new ProductDataModel();
            productDataModel.setProductimage(convertImageViewToByteArray(productaddimage));
            productDataModel.setProductname(productaddname.getText().toString());
            productDataModel.setProductquantity(Integer.parseInt(productaddquantity.getText().toString()));
            productDataModel.setProductprice(Double.parseDouble(productaddprice.getText().toString()));
            productDataModel.setProductdescription(productadddes.getText().toString());
            productDataModel.setProductstatus(prdstatus);
            productDataModel.setProductcategoryid(procatidlist);

            long result = db.productadd(productDataModel);
            if (result == -1) {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Succcess", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProdActivity.this, ListProdActivity.class);
                intent.putExtra("idd", procatidlist);
                startActivity(intent);
            }
        });

        //back to productlistactivity
        backicon.setOnClickListener(v -> {
            Intent intent = new Intent(AddProdActivity.this, ListProdActivity.class);
            intent.putExtra("idd", procatidlist);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                //for camera
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
                productaddimage.setImageBitmap(img);
            } else if (requestCode == GALLERY_CODE) {
                //for gallery
                productaddimage.setImageURI(data.getData());
            }
        }
    }

    private byte[] convertImageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}