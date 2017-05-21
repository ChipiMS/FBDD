create database banco;
use banco;
create table sucursal(
	numSucursal integer not null,
	nombreSucursal varchar(40),
	ciudad varchar(40),
	direccion varchar(40),
	constraint sucursalPK primary key (numSucursal)
);
create table empleado(
	numEmpleado integer not null,
	nombreEmpleado varchar(40),
	telefono integer,
	numSucursal int(40),
	constraint empleadoPK primary key (numEmpleado),
	constraint empleadoFK1 foreign key (numSucursal) references sucursal(numSucursal)
);
create table cliente(
	seguroSocial varchar(20) not null,
	nombreCliente varchar(40),
	ciudad varchar(40),
	calle varchar(40),
	constraint clientePK primary key (seguroSocial)
);
create table cuenta(
	numCuenta integer not null,
	saldo double,
	seguroSocial varchar(20),
	numSucursal integer,
	constraint cuentaPK primary key (numCuenta),
	constraint cuentaFK1 foreign key (seguroSocial) references cliente(seguroSocial),
	constraint cuentaFK2 foreign key (numSucursal) references sucursal(numSucursal)
);
create table transaccion(
	numTran integer not null,
	cantidad double,
	fecha date,
	numCuenta integer,
	constraint transaccionPK primary key (numTran),
	constraint transaccionFK1 foreign key (numCuenta) references cuenta(numCuenta)
);
grant all privileges on banco.* to chipi@localhost identified by 'userBanco';