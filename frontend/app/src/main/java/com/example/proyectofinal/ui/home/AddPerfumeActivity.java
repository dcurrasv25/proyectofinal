package com.example.proyectofinal.ui.home;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.model.Categoria;
import com.example.proyectofinal.data.model.Perfume;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPerfumeActivity extends AppCompatActivity {

    private EditText etNombre, etMarca, etTipo, etGenero, etPrecio, etImagen;
    private Spinner spinnerCategoria;
    private Button btnGuardar, btnSelectNotas;
    private android.widget.ImageButton btnAddCategoria, btnAddNota;
    private android.widget.TextView tvSelectedNotas;

    private List<Categoria> categoriasList = new ArrayList<>();
    private List<String> categoriasNombres = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    private List<com.example.proyectofinal.data.model.Nota> notasList = new ArrayList<>();
    private String[] notasNombres = new String[0];
    private boolean[] checkedNotas = new boolean[0];
    private List<Integer> selectedNotasIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_perfume);

        etNombre = findViewById(R.id.etNombre);
        etMarca = findViewById(R.id.etMarca);
        etTipo = findViewById(R.id.etTipo);
        etGenero = findViewById(R.id.etGenero);
        etPrecio = findViewById(R.id.etPrecio);
        etImagen = findViewById(R.id.etImagen);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnSelectNotas = findViewById(R.id.btnSelectNotas);
        tvSelectedNotas = findViewById(R.id.tvSelectedNotas);
        btnAddCategoria = findViewById(R.id.btnAddCategoria);
        btnAddNota = findViewById(R.id.btnAddNota);
        btnGuardar = findViewById(R.id.btnGuardar);

        configurarSpinner();
        cargarCategorias();
        cargarNotas();

        btnSelectNotas.setOnClickListener(v -> mostrarDialogoNotas());
        btnAddCategoria.setOnClickListener(v -> mostrarDialogoNuevaCategoria());
        btnAddNota.setOnClickListener(v -> mostrarDialogoNuevaNota());
        btnGuardar.setOnClickListener(v -> guardarPerfume());
    }

    private void configurarSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasNombres);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinnerAdapter);
    }

    private void cargarCategorias() {
        RetrofitClient.getApiService().getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriasList.clear();
                    categoriasNombres.clear();
                    
                    categoriasList.addAll(response.body());
                    for (Categoria cat : categoriasList) {
                        categoriasNombres.add(cat.getNombre());
                    }
                    spinnerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AddPerfumeActivity.this, "Error al cargar categorías", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(AddPerfumeActivity.this, "Error de red al cargar categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarNotas() {
        RetrofitClient.getApiService().getNotas().enqueue(new Callback<List<com.example.proyectofinal.data.model.Nota>>() {
            @Override
            public void onResponse(Call<List<com.example.proyectofinal.data.model.Nota>> call, Response<List<com.example.proyectofinal.data.model.Nota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notasList.clear();
                    notasList.addAll(response.body());
                    
                    notasNombres = new String[notasList.size()];
                    checkedNotas = new boolean[notasList.size()];
                    
                    for (int i = 0; i < notasList.size(); i++) {
                        notasNombres[i] = notasList.get(i).getNombre();
                        checkedNotas[i] = false;
                    }
                } else {
                    Toast.makeText(AddPerfumeActivity.this, "Error al cargar notas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<com.example.proyectofinal.data.model.Nota>> call, Throwable t) {
                Toast.makeText(AddPerfumeActivity.this, "Error de red al cargar notas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoNotas() {
        if (notasList.isEmpty()) {
            Toast.makeText(this, "No hay notas disponibles o no se han cargado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reset check status based on current selection
        for (int i = 0; i < checkedNotas.length; i++) {
            checkedNotas[i] = selectedNotasIds.contains(notasList.get(i).getId());
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Notas Aromáticas");
        builder.setMultiChoiceItems(notasNombres, checkedNotas, (dialog, which, isChecked) -> {
            checkedNotas[which] = isChecked;
        });

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            selectedNotasIds.clear();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < checkedNotas.length; i++) {
                if (checkedNotas[i]) {
                    selectedNotasIds.add(notasList.get(i).getId());
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(notasList.get(i).getNombre());
                }
            }
            
            if (selectedNotasIds.isEmpty()) {
                tvSelectedNotas.setText("Ninguna nota seleccionada");
            } else {
                tvSelectedNotas.setText("Seleccionadas: " + sb.toString());
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoNuevaCategoria() {
        EditText input = new EditText(this);
        input.setHint("Nombre de la categoría");
        input.setSingleLine(true);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        android.widget.FrameLayout container = new android.widget.FrameLayout(this);
        container.setPadding(padding, padding / 2, padding, 0);
        container.addView(input);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Nueva Categoría");
        builder.setView(container);
        builder.setPositiveButton("Crear", (dialog, which) -> {
            String nombre = input.getText().toString().trim();
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            Categoria nuevaCat = new Categoria();
            nuevaCat.setNombre(nombre);
            RetrofitClient.getApiService().crearCategoria(nuevaCat).enqueue(new Callback<Categoria>() {
                @Override
                public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(AddPerfumeActivity.this, "Categoría creada con éxito", Toast.LENGTH_SHORT).show();
                        cargarCategorias(); // Recargar spinner
                    } else {
                        Toast.makeText(AddPerfumeActivity.this, "Error al crear categoría (ya existe o no autorizado)", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Categoria> call, Throwable t) {
                    Toast.makeText(AddPerfumeActivity.this, "Error de red al crear categoría", Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoNuevaNota() {
        EditText input = new EditText(this);
        input.setHint("Nombre de la nota aromática");
        input.setSingleLine(true);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        android.widget.FrameLayout container = new android.widget.FrameLayout(this);
        container.setPadding(padding, padding / 2, padding, 0);
        container.addView(input);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Nueva Nota Aromática");
        builder.setView(container);
        builder.setPositiveButton("Crear", (dialog, which) -> {
            String nombre = input.getText().toString().trim();
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            com.example.proyectofinal.data.model.Nota nuevaNota = new com.example.proyectofinal.data.model.Nota();
            nuevaNota.setNombre(nombre);
            RetrofitClient.getApiService().crearNota(nuevaNota).enqueue(new Callback<com.example.proyectofinal.data.model.Nota>() {
                @Override
                public void onResponse(Call<com.example.proyectofinal.data.model.Nota> call, Response<com.example.proyectofinal.data.model.Nota> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(AddPerfumeActivity.this, "Nota creada con éxito", Toast.LENGTH_SHORT).show();
                        cargarNotas(); // Recargar lista de notas
                    } else {
                        Toast.makeText(AddPerfumeActivity.this, "Error al crear nota (ya existe o no autorizado)", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<com.example.proyectofinal.data.model.Nota> call, Throwable t) {
                    Toast.makeText(AddPerfumeActivity.this, "Error de red al crear nota", Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void guardarPerfume() {
        String nombre = etNombre.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String tipo = etTipo.getText().toString().trim();
        String genero = etGenero.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String imagen = etImagen.getText().toString().trim();

        if (nombre.isEmpty() || marca.isEmpty() || tipo.isEmpty() || genero.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedNotasIds.isEmpty()) {
            Toast.makeText(this, "Debes seleccionar al menos una nota aromática", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Introduce un precio válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoriasList.isEmpty()) {
            Toast.makeText(this, "Aún no se han cargado las categorías", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPos = spinnerCategoria.getSelectedItemPosition();
        int categoriaId = categoriasList.get(selectedPos).getId();

        Perfume nuevoPerfume = new Perfume();
        nuevoPerfume.setNombre(nombre);
        nuevoPerfume.setMarca(marca);
        nuevoPerfume.setTipo(tipo);
        nuevoPerfume.setGenero(genero);
        nuevoPerfume.setPrecio(precio);
        nuevoPerfume.setCategoria(categoriaId);
        nuevoPerfume.setNotas(selectedNotasIds);
        nuevoPerfume.setImagen(imagen.isEmpty() ? null : imagen);

        RetrofitClient.getApiService().crearPerfume(nuevoPerfume).enqueue(new Callback<Perfume>() {
            @Override
            public void onResponse(Call<Perfume> call, Response<Perfume> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddPerfumeActivity.this, "Perfume creado exitosamente", Toast.LENGTH_SHORT).show();
                    finish(); // Volver y cerrar actividad
                } else {
                    Toast.makeText(AddPerfumeActivity.this, "Error al crear perfume: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Perfume> call, Throwable t) {
                Toast.makeText(AddPerfumeActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
