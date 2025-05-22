# Relaciones en Laravel: Uso de Tablas Pivote

## 1. Tipos de relaciones en Eloquent

- **Uno a uno:** `hasOne`, `belongsTo`
- **Uno a muchos:** `hasMany`, `belongsTo`
- **Muchos a muchos:** `belongsToMany`
- **Uno a través de otro:** `hasOneThrough`, `hasManyThrough`

---

## 2. Relación muchos a muchos (tabla pivote)

### Ejemplo: `User` y `Role` usando `role_user`

### Migración de tabla pivote

```php
Schema::create('role_user', function (Blueprint $table) {
    $table->id();
    $table->foreignId('user_id')->constrained()->onDelete('cascade');
    $table->foreignId('role_id')->constrained()->onDelete('cascade');
    $table->unsignedBigInteger('assigned_by')->nullable();
    $table->timestamps();
});
```

---

## 3. Relaciones en los modelos

### User.php

```php
public function roles()
{
    return $this->belongsToMany(Role::class)
                ->withPivot('assigned_by')
                ->withTimestamps();
}
```

### Role.php

```php
public function users()
{
    return $this->belongsToMany(User::class);
}
```

---

## 4. Asignar roles a un usuario

### attach (agregar)

```php
$user->roles()->attach($roleId);
$user->roles()->attach($roleId, ['assigned_by' => $adminId]);

$user->roles()->attach([
    1 => ['assigned_by' => 10],
    2 => ['assigned_by' => 10],
]);
```

### sync (reemplazar todos los roles existentes)

```php
$user->roles()->sync([1, 2]);
```

### detach (eliminar relaciones)

```php
$user->roles()->detach($roleId);
$user->roles()->detach(); // todos
```

---

## 5. Leer datos de la relación y el pivote

```php
foreach ($user->roles as $role) {
    echo $role->name;
    echo $role->pivot->assigned_by;
}
```

---

## 6. Crear usuario y asignar rol con dato pivote

```php
$user = User::create([
    'name' => 'Pedro',
    'email' => 'pedro@example.com',
    'password' => bcrypt('123456'),
]);

$user->roles()->attach($roleId, ['assigned_by' => auth()->id()]);
```

---

## 7. Actualizar datos en la tabla pivote

```php
$user->roles()->updateExistingPivot($roleId, ['assigned_by' => 5]);
```

---

## 8. Consultar por relaciones

```php
$users = User::whereHas('roles', function($q) {
    $q->where('name', 'admin');
})->get();
```

---

## 9. Modelo intermedio personalizado (opcional)

```php
public function roles()
{
    return $this->belongsToMany(Role::class)
                ->using(RoleUser::class)
                ->withPivot('assigned_by');
}
```

---

## 10. Seeder de ejemplo

```php
$user = User::factory()->create();
$role = Role::where('name', 'admin')->first();

$user->roles()->attach($role->id, ['assigned_by' => 1]);
```

Claro, te refieres al ejemplo donde usabas:

```php
$student = Student::where("cardID", $cardID)->first();
```

Y me preguntaste cómo usar `orWhere`, `and`, y cómo hacer consultas relacionadas con tablas pivote. Te preparo un bloque con esos ejemplos aplicados, enfocados en un modelo `Student` y relaciones muchos a muchos con una tabla pivote, y lo añado al archivo de apuntes:

---

### 🔍 Ejemplos adicionales de consultas condicionales

#### Consulta con `orWhere` y `and`

```php
$student = Student::where('cardID', $cardID)
                  ->orWhere('email', $email)
                  ->first();
```

> Esto devuelve el primer estudiante con ese `cardID` **o** ese `email`.

#### Consulta con múltiples condiciones `and` implícitas

```php
$students = Student::where('active', true)
                   ->where('program', 'Engineering')
                   ->get();
```

> Esto devuelve todos los estudiantes activos **y** del programa "Engineering".

---

### 📌 Relación muchos a muchos: `Student` y `Course`

#### Definición en los modelos

```php
// Student.php
public function courses()
{
    return $this->belongsToMany(Course::class)
                ->withPivot('grade', 'year')
                ->withTimestamps();
}

// Course.php
public function students()
{
    return $this->belongsToMany(Student::class);
}
```

#### Asignar curso con nota y año

```php
$student->courses()->attach($courseId, ['grade' => 90, 'year' => 2025]);
```

#### Leer nota y año desde la relación

```php
foreach ($student->courses as $course) {
    echo $course->name;
    echo $course->pivot->grade;
    echo $course->pivot->year;
}
```

#### Filtrar por nota en la tabla pivote

```php
$students = Student::whereHas('courses', function ($q) {
    $q->where('grade', '>=', 90);
})->get();
```

---

Perfecto, aquí tienes cómo hacer esa consulta y **devolver los datos como JSON**, incluyendo los **productos con nombre repetido** y **los almacenes donde están almacenados con su cantidad desde la tabla pivote**.

---

### ✅ Controlador en Laravel: Respuesta JSON

```php
use App\Models\Product;

public function kardexPorNombre($nombre)
{
    $productos = Product::where('name', $nombre)
        ->with(['warehouses' => function ($query) {
            $query->select('warehouses.id', 'name'); // Solo lo necesario
        }])
        ->get();

    $resultado = $productos->map(function ($producto) {
        return [
            'product_id' => $producto->id,
            'product_name' => $producto->name,
            'warehouses' => $producto->warehouses->map(function ($warehouse) {
                return [
                    'warehouse_id' => $warehouse->id,
                    'warehouse_name' => $warehouse->name,
                    'quantity' => $warehouse->pivot->quantity,
                ];
            }),
        ];
    });

    return response()->json($resultado);
}
```

---

### 🔗 Ruta en `routes/api.php`

```php
Route::get('/kardex/{nombre}', [TuControlador::class, 'kardexPorNombre']);
```

---

### 🧪 Ejemplo de respuesta JSON

Para `/api/kardex/Leche Entera`, el JSON puede ser:

```json
[
  {
    "product_id": 12,
    "product_name": "Leche Entera",
    "warehouses": [
      {
        "warehouse_id": 1,
        "warehouse_name": "Bodega Norte",
        "quantity": 30
      },
      {
        "warehouse_id": 2,
        "warehouse_name": "Central",
        "quantity": 10
      }
    ]
  },
  {
    "product_id": 18,
    "product_name": "Leche Entera",
    "warehouses": [
      {
        "warehouse_id": 2,
        "warehouse_name": "Central",
        "quantity": 40
      }
    ]
  }
]
```

---

¿Deseas una versión alternativa donde se **agrupen los almacenes y se sumen las cantidades** por almacén también en formato JSON?
