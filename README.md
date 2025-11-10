APP MOVIL EV2 – Aplicación Android de Interconexión de Activities

-- DESCRIPCIÓN GENERAL -- adasd 

La aplicación AppMovilEV2 fue desarrollada para mostrar cómo se comunican varias pantallas (Activities) en Android.

Simula un sistema de inicio de sesión donde se puede:
	- Iniciar sesión con validación simple.
	- Registrar una cuenta.
	- Recuperar contraseña.
	- Ver el estado del Bluetooth del dispositivo (pidiendo permiso).

-- ESTRUCTURA DEL PROYECTO --

El proyecto tiene cuatro Activities principales:

	 - SplashActivity: Muestra el logo por 2 segundos y pasa al login.

	 - LoginActivity: Pantalla principal con los botones de navegación.

	 - RegisterActivity: Permite registrar una cuenta (solo simulado).

	 - RecoverActivity: Permite recuperar la contraseña (solo simulado).

Cada pantalla usa un layout XML y un diseño basado en Material Design.

-- FLUJO DE NAVEGACIÓN --

SplashActivity → LoginActivity
Desde el Login se puede:

	Iniciar sesión (solo validación y mensaje).

	Ir a Registrar (RegisterActivity).	

	Ir a Recuperar (RecoverActivity).

	Consultar estado Bluetooth (con permiso y mensaje).

Los botones “Volver” regresan al Login usando finish().

-- MEDIDAS DE SEGURIDAD Y BUENAS PRÁCTICAS --

No se guardan datos reales ni contraseñas.

Solo se usa el permiso BLUETOOTH_CONNECT cuando el usuario lo solicita.

Se usa AlertDialog para mostrar mensajes seguros al usuario.

Código comentado, fácil de leer y mantener.


-- FUNCIONAMIENTO GENERAL --

Al iniciar, se muestra el logo (SplashActivity).

Luego pasa al Login donde el usuario puede ingresar datos o navegar.

Si los campos están vacíos, aparece un mensaje de error.

Si se completa el formulario, se muestra un mensaje de éxito.

El botón “Ver estado Bluetooth” solicita el permiso y muestra si está activado o no.
