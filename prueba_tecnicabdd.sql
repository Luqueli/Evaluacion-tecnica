create database prueba_tecnicabdd;
use prueba_tecnicabdd;


# Tabla Producto
create table Producto(
	codigo_p int AUTO_INCREMENT,
    nombre_p varchar(50) NOT NULL,
    precio_p float NOT NULL,
    categoria_p varchar(50) NOT NULL,
    constraint pk_cp primary key(codigo_p)
);

#Tabla Vendedor
create table Vendedor(
	id_v int NOT NULL AUTO_INCREMENT,
    nombre_v varchar(50) NOT NULL,
    sueldo_v float NOT NULL,
    constraint pk_idv primary key(id_v)
);

create table Venta(
	codigo_v int NOT NULL AUTO_INCREMENT,
    id_vendedor_v int NOT NULL,
    codigo_producto_v int NOT NULL,
    constraint pk_cv primary key(codigo_v),
    constraint fk_ivv foreign key(id_vendedor_v) references Vendedor (id_v),
    constraint fk_cpv foreign key(codigo_producto_v) references Producto (codigo_p)
);

############################
drop table Venta;
drop table Vendedor;
drop table Producto;
############################