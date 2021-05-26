<h2>Aplicación android para Ayuntamientos</h2>


La aplicación AppHinojos, permite a los ciudadanos estar al tanto de las noticas del pueblo, así como contactar con el ayuntamiento, enviar incidencias, ver sitios de interés, listín telefónico, historia del pueblo, etc.

<img align="center" width="306" height="530" src="https://i.ibb.co/XyWV3yw/Screenshot-2.jpg">

En el menú encontramos diferentes funcionalidades:

+ <b>Historia:</b> Pequeño texto con la historia del pueblo y algunas imágenes.

<img aling="center" width="306" height="530" src="https://i.ibb.co/xX4BWMy/Screenshot-3.jpg">

+ <b>Ocio:</b> En ocio tendremos tres opciones, ¿Qué visitar?, ¿Dónde comer? Y ¿Dónde dormir?, si se pulsa en cada una de esas se nos abre un mapa interactivo de Google con diferentes marcadores, pinchando en cada marcador se nos abrirá un globo que nos permitirá cargar una web embebida dentro de la app.

<img aling="center" width="306" height="530" src="https://i.ibb.co/4WvWvYy/Screenshot-4.jpg">

+ <b>Teléfonos:</b> mostrará un listado de teléfonos, con los teléfonos del pueblo, y junto al contacto aparecerá una imagen de un pequeño teléfono, si pulsamos sobre él, se abrirá la aplicación teléfono, del dispositivo Android, con el número ya marcado listo para llamar.

<img aling="center" width="306" height="530" src="https://i.ibb.co/WBqjJMt/Screenshot-6.jpg">

+ <b>Noticias:</b> este es uno de los apartados más importantes de la aplicación, ya que desde aquí se va a descargar las noticias que hay alojada en la base de datos de la web, por eso, hay que verificar que hay internet para evitar errores y controlarlos.
Para obtener las noticias, la app se conectará a la url de un web service que está ofreciendo un json, este json lo decodificará y lo mostrará de la forma adecuada. La implantación de esta api rest en nuestro hosting, se explicará en los apartados posteriores
Para añadir una nueva noticia a nuestra aplicación, el administrador solo tendrá que acceder a la web de administración y en el apartado noticias, añadir una nueva.

<img aling="center" width="306" height="530" src="https://i.ibb.co/tqkjXyZ/Screenshot-10.jpg">

+ <b>Contactar:</b> esta opción va destinada a que los usuarios puedan resolver cualquier duda con el ayuntamiento.
Al entrar en esta activity, nos aparecerá un formulario, con los campos nombre, email, pregunta y un botón enviar. Cuando el usuario haya rellenado de forma correcta los campos, podrá pulsar el botón enviar, con esto la aplicación se conecta al web service del servidor y le pasa unos datos, dicho datos cuando lo recibe el web service, lo almacena en una tabla de la bd, y el administrador de la web podrá ver la consulta y respondérsela.

+ <b>Notificar incidencia:</b> este apartado es similar a contactar, pero va destinado a que los usuarios de la app puedan registrar una incidencia en el pueblo, con la mayor brevedad y a fin de evitar problemas mayores haciendo uso del GPS.

<img aling="center" width="306" height="530" src="https://i.ibb.co/nRzN23f/Screenshot-8.jpg"> &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  <img aling="center" width="306" height="530" src="https://i.ibb.co/ZmrDnVK/Screenshot-9.jpg">


