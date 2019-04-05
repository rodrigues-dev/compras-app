package br.rodrigues.compras.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import br.rodrigues.compras.R;
import br.rodrigues.compras.dao.ItemDAO;
import br.rodrigues.compras.model.Item;

public class ListaItensActivity extends AppCompatActivity {

    public static final String TITLE_APPBAR = "Lista";
    private ListaItensHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_itens);
        setTitle(TITLE_APPBAR);
        helper = new ListaItensHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Limpar lista").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                helper.cleanList();
                helper.updatedListItems();
                helper.subtotalCalculation();
                Toast.makeText(ListaItensActivity.this, "Lista limpada", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        menu.add("Deletar lista").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                helper.deleteList();
                Toast.makeText(ListaItensActivity.this, "Lista deletada", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        menu.add("Deletar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                Item itemToDelete = helper.listaItensGetItemAtPosition(menuInfo.position);

                ItemDAO dao = new ItemDAO(ListaItensActivity.this);

                dao.delete(itemToDelete);

                helper.updatedListItems();
                helper.totalCalculation();
                helper.subtotalCalculation();

                Toast.makeText(ListaItensActivity.this, "Item deletado", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        menu.add("Editar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                Item itemToUpdate = helper.listaItensGetItemAtPosition(menuInfo.position);

                Intent goToUpdate = new Intent(ListaItensActivity.this, FormularioActivity.class);

                goToUpdate.putExtra("itemToUpdate", itemToUpdate);

                startActivity(goToUpdate);
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.updatedListItems();
        helper.totalCalculation();
        helper.subtotalCalculation();
    }
}