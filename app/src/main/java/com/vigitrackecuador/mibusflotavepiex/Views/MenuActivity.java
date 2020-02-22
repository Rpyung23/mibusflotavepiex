package com.vigitrackecuador.mibusflotavepiex.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.vigitrackecuador.mibusflotavepiex.R;
import com.vigitrackecuador.mibusflotavepiex.fragments.BlankFragment;
import com.vigitrackecuador.mibusflotavepiex.fragments.DespachoFragment;
import com.vigitrackecuador.mibusflotavepiex.fragments.ProduccFragment;
import com.vigitrackecuador.mibusflotavepiex.fragments.RutasFragment;

public class MenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navi_vista);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.title_toolbar_drawer,R.string.title_toolbar_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.id_rutas:
                        Toast.makeText(MenuActivity.this,"RUTAS",Toast.LENGTH_SHORT).show();
                        RutasFragment oR= new RutasFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oR)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
                        break;
                    case R.id.id_despacho:
                        Toast.makeText(MenuActivity.this,"DESPACHO",Toast.LENGTH_SHORT).show();
                        DespachoFragment oD= new DespachoFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oD)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
                        break;
                    case R.id.id_conteo:
                        ProduccFragment oP = new ProduccFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oP)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        BlankFragment oB= new BlankFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oB)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

   /* @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        {
            super.onBackPressed();
        }
    }*/

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (  Integer.valueOf(android.os.Build.VERSION.SDK) < 7 //Instead use android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // This will be called either automatically for you on 2.0
        // or later, or by the code above on earlier versions of the
        // platform.
        return;
    }
}
