package com.imorning.qmc_decoder;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.Collections;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {
    private String[] permissionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            runMainActivity();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            permissionList = new String[]{Permission.MANAGE_EXTERNAL_STORAGE};
        } else {
            permissionList = Permission.Group.STORAGE;
        }
        XXPermissions.with(PermissionActivity.this)
                .permission(permissionList)
                .request((permissions, all) -> {
                    if (!all) {
                        showNeedPermissionDialog(permissions);
                    } else {
                        runMainActivity();
                    }
                });
    }

    private void showNeedPermissionDialog(List<String> permissions) {
        new AlertDialog.Builder(PermissionActivity.this)
                .setMessage("请授权app权限，否则无法正常运行！")
                .setCancelable(false)
                .setPositiveButton("确定", (dialog, which) -> {
                    XXPermissions.startPermissionActivity(PermissionActivity.this, permissions);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    finish();
                })
                .create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, permissionList)) {
                runMainActivity();
            } else {
                showNeedPermissionDialog(Collections.singletonList(Permission.WRITE_EXTERNAL_STORAGE));
            }
        }
    }

    private void runMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}