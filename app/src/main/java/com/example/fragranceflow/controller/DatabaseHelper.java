package com.example.fragranceflow.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fragranceflow.model.Customer;
import com.example.fragranceflow.model.Product;
import com.example.fragranceflow.model.Sale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbApp.db";
    private static final int DATABASE_VERSION = 1;

    // Tabelas do banco
    public static final String TABLE_PRODUCTS = "produtos";
    private static final String TABLE_CUSTOMERS = "clientes";
    private static final String TABLE_SALES = "vendas";

    // Colunas da Tabela Produtos
    public static final String COLUMN_PRODUTO_ID = "_id";
    public static final String COLUMN_PRODUTO_NOME = "nome";
    public static final String COLUMN_PRODUTO_QUANTIDADE = "quantidade";
    public static final String COLUMN_PRODUTO_MARCA = "marca";
    public static final String COLUMN_PRODUTO_CUSTO_UNITARIO = "custo_unitario";
    public static final String COLUMN_PRODUTO_MARGEM_LUCRO = "margem_lucro";
    public static final String COLUMN_PRODUTO_VALOR_VENDA = "valor_venda";
    public static final String COLUMN_PRODUTO_CATEGORIA = "categoria";
    public static final String COLUMN_PRODUTO_VOLUME = "volume";
    public static final String COLUMN_PRODUTO_VALIDADE = "validade";

    // Colunas da Tabela Cliente
    private static final String COLUMN_CLIENTE_ID = "id";
    private static final String COLUMN_CLIENTE_NOME = "nome";
    private static final String COLUMN_CLIENTE_TELEFONE = "telefone";

    // Colunas da Tabela Venda
    private static final String COLUMN_VENDA_ID = "id";
    private static final String COLUMN_VENDA_CLIENTE_ID = "cliente_id";
    private static final String COLUMN_VENDA_PRODUTO_ID = "produto_id";
    private static final String COLUMN_VENDA_QUANTIDADE = "quantidade";
    private static final String COLUMN_VENDA_DATA = "data_venda";

    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUTO_NOME + " TEXT, " +
                    COLUMN_PRODUTO_QUANTIDADE + " INTEGER," +
                    COLUMN_PRODUTO_MARCA + " TEXT," +
                    COLUMN_PRODUTO_CUSTO_UNITARIO + " FLOAT," +
                    COLUMN_PRODUTO_MARGEM_LUCRO + " INTEGER," +
                    COLUMN_PRODUTO_VALOR_VENDA + " FLOAT," +
                    COLUMN_PRODUTO_CATEGORIA + " TEXT," +
                    COLUMN_PRODUTO_VOLUME + " TEXT," +
                    COLUMN_PRODUTO_VALIDADE + " TEXT);";


    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE " + TABLE_CUSTOMERS + " (" +
            COLUMN_CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CLIENTE_NOME + " TEXT, " +
            COLUMN_CLIENTE_TELEFONE + " TEXT" + ")";

    private static final String CREATE_TABLE_SALES = "CREATE TABLE " + TABLE_SALES + " (" +
            COLUMN_VENDA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_VENDA_CLIENTE_ID + " INTEGER, " +
            COLUMN_VENDA_PRODUTO_ID + " INTEGER, " +
            COLUMN_VENDA_QUANTIDADE + " INTEGER, " +
            COLUMN_VENDA_DATA + " TEXT, " +
            "FOREIGN KEY(" + COLUMN_VENDA_CLIENTE_ID + ") REFERENCES " + TABLE_CUSTOMERS + "(" + COLUMN_CLIENTE_ID + "), " +
            "FOREIGN KEY(" + COLUMN_VENDA_PRODUTO_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUTO_ID + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_CUSTOMERS);
        db.execSQL(CREATE_TABLE_SALES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        onCreate(db);
    }

    // Métodos CRUD para produtos

    public long addProduct(Product produto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUTO_NOME, produto.getNome());
        values.put(COLUMN_PRODUTO_QUANTIDADE, produto.getQuantidade());
        values.put(COLUMN_PRODUTO_MARCA, produto.getMarca());
        values.put(COLUMN_PRODUTO_CUSTO_UNITARIO, produto.getCustoUnitario());
        values.put(COLUMN_PRODUTO_MARGEM_LUCRO, produto.getMargemLucro());
        values.put(COLUMN_PRODUTO_VALOR_VENDA, produto.getValorVenda());
        values.put(COLUMN_PRODUTO_CATEGORIA, produto.getCategoria());
        values.put(COLUMN_PRODUTO_VOLUME, produto.getVolume());
        values.put(COLUMN_PRODUTO_VALIDADE, produto.getValidade());
        return db.insert(TABLE_PRODUCTS,null, values);
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE_PRODUCTS, new String[] {COLUMN_PRODUTO_ID, COLUMN_PRODUTO_NOME, COLUMN_PRODUTO_QUANTIDADE, COLUMN_PRODUTO_MARCA, COLUMN_PRODUTO_CUSTO_UNITARIO, COLUMN_PRODUTO_MARGEM_LUCRO, COLUMN_PRODUTO_VALOR_VENDA, COLUMN_PRODUTO_CATEGORIA, COLUMN_PRODUTO_VOLUME, COLUMN_PRODUTO_VALIDADE},
                    COLUMN_PRODUTO_ID + "=?", new String[] {String.valueOf(id)},
                    null, null, null, null);
            // Verifica se o cursor retornou algum resultado
            if (cursor != null && cursor.moveToFirst()) {
                // Era: cursor.moveToFirst();
                Product produto = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getFloat(4), cursor.getInt(5), cursor.getFloat(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                cursor.close();
                return produto;
            } else {
                Log.w("DatabaseHelper", "Produto com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao obter produto: " + e.getMessage(), e);
        }
        db.close();

        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> produtos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Modifiquei, estava: cursor.moveToFirst(). Tentar assim: cursor != null && cursor.moveToFirst()
        if (cursor.moveToFirst()) {
            do {
                Product produto = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getFloat(4), cursor.getInt(5), cursor.getFloat(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                produtos.add(produto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return produtos;
    }

    public int updateProduct(Product produto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUTO_NOME, produto.getNome());
        values.put(COLUMN_PRODUTO_QUANTIDADE, produto.getQuantidade());
        values.put(COLUMN_PRODUTO_MARCA, produto.getMarca());
        values.put(COLUMN_PRODUTO_CUSTO_UNITARIO, produto.getCustoUnitario());
        values.put(COLUMN_PRODUTO_MARGEM_LUCRO, produto.getMargemLucro());
        values.put(COLUMN_PRODUTO_VALOR_VENDA, produto.getValorVenda());
        values.put(COLUMN_PRODUTO_CATEGORIA, produto.getCategoria());
        values.put(COLUMN_PRODUTO_VOLUME, produto.getVolume());
        values.put(COLUMN_PRODUTO_VALIDADE, produto.getValidade());
        return db.update(TABLE_PRODUCTS, values, COLUMN_PRODUTO_ID + "=?", new String[] {String.valueOf(produto.getId())});
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_PRODUTO_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }


    // Métodos CRUD para clientes

    public long addCustomer(Customer cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Verifica se o telefone está no formato correto usando regex
        if (!cliente.isTelefoneValido()) {
            throw new IllegalArgumentException("Telefone inválido. Tente algo como: 083996445577");
        }

        values.put(COLUMN_CLIENTE_NOME, cliente.getNome());
        values.put(COLUMN_CLIENTE_TELEFONE, cliente.getTelefone());

        // Inserindo o cliente no banco de dados
        long id = db.insert(TABLE_CUSTOMERS, null, values);

        // Fecha banco
        db.close();

        return id; //Era: db.insert(TABLE_CUSTOMERS,null, values);
    }

    public Customer getCustomer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE_CUSTOMERS, new String[] {COLUMN_CLIENTE_ID, COLUMN_CLIENTE_NOME, COLUMN_CLIENTE_TELEFONE},
                    COLUMN_CLIENTE_ID + "=?", new String[] {String.valueOf(id)},
                    null, null, null, null);
            // Verifica se o cursor retornou algum resultado
            if (cursor != null && cursor.moveToFirst()) {
                // Era: cursor.moveToFirst();
                Customer cliente = new Customer(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                cursor.close();
                return cliente;
            } else {
                Log.w("DatabaseHelper", "Cliente com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao obter cliente: " + e.getMessage(), e);
        }
        db.close();
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> clientes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Modifiquei, estava: cursor.moveToFirst(). Tentar assim: cursor != null && cursor.moveToFirst()
        if (cursor.moveToFirst()) {
            do {
                Customer cliente = new Customer(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                clientes.add(cliente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return clientes;
    }

    public int updateCustomer(Customer cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLIENTE_NOME, cliente.getNome());
        values.put(COLUMN_CLIENTE_TELEFONE, cliente.getTelefone());
        return db.update(TABLE_CUSTOMERS, values, COLUMN_CLIENTE_ID + "=?", new String[] {String.valueOf(cliente.getId())});
    }

    // Métodos CRUD para vendas
    public long addSale(Sale venda) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Verifica se o cliente existe
        Customer cliente = getCustomer(venda.getClienteId());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com ID " + venda.getClienteId() + " não encontrado."); // Cliente não encontrado
        }

        // Verifica se o produto existe
        Product produto = getProduct(venda.getProdutoId());
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + venda.getProdutoId() + " não encontrado."); // Produto não encontrado
        }

        // Verifica se tem produto suficiente para vender
        if (produto.getQuantidade() < venda.getQuantidade()) {
            throw new IllegalArgumentException("Quantidade insuficiente para o produto: " + produto.getNome()); // Quantidade insuficiente. Era assim(Funcionaria melhor para textErroVenda na view da tela): throw new IllegalArgumentException("Quantidade insuficiente para o produto: " + produto.getNome() + ". Quantidade disponível: " + produto.getQuantidade());
        }

        values.put(COLUMN_VENDA_CLIENTE_ID, venda.getClienteId());
        values.put(COLUMN_VENDA_PRODUTO_ID, venda.getProdutoId());
        values.put(COLUMN_VENDA_QUANTIDADE, venda.getQuantidade());
        values.put(COLUMN_VENDA_DATA, venda.getDataVenda());

        // Insere a venda
        long vendaId = db.insert(TABLE_SALES, null, values);

        // Atualiza a quantidade do produto do estoque
        produto.setQuantidade(produto.getQuantidade() - venda.getQuantidade());
        updateProduct(produto);

        db.close();
        return vendaId; // Era: return db.insert(TABLE_SALES,null, values);

    }

    public Sale getSale(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SALES, new String[] {COLUMN_VENDA_ID, COLUMN_VENDA_CLIENTE_ID, COLUMN_VENDA_PRODUTO_ID, COLUMN_VENDA_QUANTIDADE, COLUMN_VENDA_DATA},
                COLUMN_VENDA_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Sale venda = new Sale(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
            cursor.close();
            return venda;
        }
        return null;
    }

    public List<Sale> getAllSales() {
        List<Sale> vendas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SALES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Modifiquei, estava: cursor.moveToFirst(). Tentar assim: cursor != null && cursor.moveToFirst()
        if (cursor.moveToFirst()) {
            do {
                Sale venda = new Sale(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
                vendas.add(venda);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vendas;
    }

    public int updateSale(Sale venda) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VENDA_CLIENTE_ID, venda.getClienteId());
        values.put(COLUMN_VENDA_PRODUTO_ID, venda.getProdutoId());
        values.put(COLUMN_VENDA_QUANTIDADE, venda.getQuantidade());
        values.put(COLUMN_VENDA_DATA, venda.getDataVenda());
        return db.update(TABLE_SALES, values, COLUMN_VENDA_ID + "=?", new String[] {String.valueOf(venda.getId())});
    }

    public double getValorTotalVenda(int vendaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double valorTotal = 0;

        String query = "SELECT v.quantidade, p.valor_venda " +
                "FROM " + TABLE_SALES + " v " +
                "JOIN " + TABLE_PRODUCTS + " p ON v.produto_id = p._id " +
                "WHERE v.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(vendaId)});

        if (cursor != null && cursor.moveToFirst()) {
            int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
            double valorVenda = cursor.getDouble(cursor.getColumnIndexOrThrow("valor_venda"));
            valorTotal = quantidade * valorVenda;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return valorTotal;
    }

    public double getLucroVenda(int vendaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double lucro = 0;

        String query = "SELECT v.quantidade, p.valor_venda, p.custo_unitario " +
                "FROM " + TABLE_SALES + " v " +
                "JOIN " + TABLE_PRODUCTS + " p ON v.produto_id = p._id " +
                "WHERE v.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(vendaId)});

        if (cursor != null && cursor.moveToFirst()) {
            int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
            double valorVenda = cursor.getDouble(cursor.getColumnIndexOrThrow("valor_venda"));
            double custoUnitario = cursor.getDouble(cursor.getColumnIndexOrThrow("custo_unitario"));
            double valorTotalVenda = quantidade * valorVenda;
            double custoTotal = quantidade * custoUnitario;
            lucro = valorTotalVenda - custoTotal;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return lucro;
    }

    public double getMargemVenda(int vendaId) {
        double valorTotal = getValorTotalVenda(vendaId);
        double lucro = getLucroVenda(vendaId);

        if (valorTotal == 0) {
            return 0;
        }

        return (lucro / valorTotal) * 100;
    }

    public List<Sale> getVendasPorData(String dataInicio, String dataFinal) {
        List<Sale> listaVendas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SALES +
                " WHERE data_venda BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(query, new String[]{dataInicio, dataFinal});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id"));
                int produtoId = cursor.getInt(cursor.getColumnIndexOrThrow("produto_id"));
                int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
                String dataVenda = cursor.getString(cursor.getColumnIndexOrThrow("data_venda"));

                // Criar um objeto Venda e adicionar à lista
                Sale venda = new Sale(id, clienteId, produtoId, quantidade, dataVenda);
                listaVendas.add(venda);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return listaVendas;
    }

    public double getValorTotalVendasNoPeriodo(String dataInicio, String dataFinal) {
        SQLiteDatabase db = this.getReadableDatabase();
        double valorTotal = 0;

        String query = "SELECT v.quantidade, p.valor_venda " +
                "FROM " + TABLE_SALES + " v " +
                "JOIN " + TABLE_PRODUCTS + " p ON v.produto_id = p._id " +
                "WHERE v.data_venda BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(query, new String[]{dataInicio, dataFinal});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
                double valorVenda = cursor.getDouble(cursor.getColumnIndexOrThrow("valor_venda"));
                valorTotal += quantidade * valorVenda;
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return valorTotal;
    }

    public double getLucroTotalVendasNoPeriodo(String dataInicio, String dataFinal) {
        SQLiteDatabase db = this.getReadableDatabase();
        double lucroTotal = 0;

        String query = "SELECT v.quantidade, p.valor_venda, p.custo_unitario " +
                "FROM " + TABLE_SALES + " v " +
                "JOIN " + TABLE_PRODUCTS + " p ON v.produto_id = p._id " +
                "WHERE v.data_venda BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(query, new String[]{dataInicio, dataFinal});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
                double valorVenda = cursor.getDouble(cursor.getColumnIndexOrThrow("valor_venda"));
                double custoUnitario = cursor.getDouble(cursor.getColumnIndexOrThrow("custo_unitario"));
                double valorTotalVenda = quantidade * valorVenda;
                double custoTotal = quantidade * custoUnitario;
                lucroTotal += (valorTotalVenda - custoTotal);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return lucroTotal;
    }

}
