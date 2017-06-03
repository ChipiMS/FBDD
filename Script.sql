create database banco;
use banco;
create table sucursal(
	numSucursal integer not null,
	nombreSucursal varchar(40),
	ciudad varchar(40),
	direccion varchar(40),
	constraint sucursalPK primary key (numSucursal)
);
create table tipoEmpleado(
	claveTipoEmpleado varchar(4) not null,
	descripcion varchar(20),
	constraint tipoEmpleadoPK primary key (claveTipoEmpleado)
);
create table empleado(
	numEmpleado integer not null,
	nombreEmpleado varchar(40),
	telefono char(10),
	numSucursal integer,
	claveTipoEmpleado varchar(4),
	constraint empleadoPK primary key (numEmpleado),
	constraint empleadoFK1 foreign key (numSucursal) references sucursal(numSucursal),
	constraint empleadoFK2 foreign key (claveTipoEmpleado) references tipoEmpleado(claveTipoEmpleado)
);
create table cliente(
	seguroSocial varchar(20) not null,
	nombreCliente varchar(40),
	ciudad varchar(40),
	calle varchar(40),
	constraint clientePK primary key (seguroSocial)
);
create table tipoCuenta(
	claveTipoCuenta varchar(4) not null,
	descripcion varchar(20),
	constraint tipoCuentaPK primary key (claveTipoCuenta)
);
create table cuenta(
	numCuenta integer not null,
	saldo double,
	seguroSocial varchar(20),
	numSucursal integer,
	claveTipoCuenta varchar(4),
	constraint cuentaPK primary key (numCuenta),
	constraint cuentaFK1 foreign key (seguroSocial) references cliente(seguroSocial),
	constraint cuentaFK2 foreign key (numSucursal) references sucursal(numSucursal),
	constraint cuentaFK3 foreign key (claveTipoCuenta) references tipoCuenta(claveTipoCuenta)
);
create table tipoTransaccion(
	claveTipoTransaccion varchar(4) not null,
	descripcion varchar(20),
	constraint tipoTransaccionPK primary key (claveTipoTransaccion)
);
create table transaccion(
	numTran integer not null,
	cantidad double,
	fecha date,
	numCuenta integer,
	claveTipoTransaccion varchar(4),
	constraint transaccionPK primary key (numTran),
	constraint transaccionFK1 foreign key (numCuenta) references cuenta(numCuenta),
	constraint transaccionFK2 foreign key (claveTipoTransaccion) references tipoTransaccion(claveTipoTransaccion)
);
grant all privileges on banco.* to chipi@localhost identified by 'userBanco';