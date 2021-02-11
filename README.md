# Taller De Arquitecturas De Servidores De Aplicaciones, Meta Protocolos De Objetos, Patrón IOC y Reflexión

[![CircleCI](https://circleci.com/gh/Silenrate/AREP-Lab4.svg?style=svg)](https://app.circleci.com/pipelines/github/Silenrate/AREP-Lab4)

[![Deployed to Heroku](https://www.herokucdn.com/deploy/button.png)](https://arep-walteros-lab4.herokuapp.com/)

Fecha: Viernes, 12 de Febrero del 2021

Cuarto Laboratorio de Arquitecturas Empresariales (AREP).

Para este taller los estudiantes deberán construir un servidor Web (tipo Apache) en Java. El servidor debe ser capaz de entregar páginas html e imágenes tipo PNG. Igualmente el servidor debe proveer un framework IoC para la construcción de aplicaciones web a partir de POJOS. Usando el servidor se debe construir una aplicación Web de ejemplo y desplegarlo en Heroku. El servidor debe atender múltiples solicitudes no concurrentes.

Para este taller desarrolle un prototipo mínimo que demuestre capacidades reflexivas de JAVA y permita por lo menos cargar un bean (POJO) y derivar una aplicación Web a partir de él. Debe entregar su trabajo al final del laboratorio.

**SUGERENCIAS**

1. Cargue el POJO desde la línea de comandos, de manera similar al framework de TEST. Es decir pásela como parámetro cuando invoke el framework. 

   Ejemplo de invocación:

   `java -cp target/classes co.edu.escuelaing.reflexionlab.MicroSpringBoot co.edu.escuelaing.reflexionlab.FirstWebService`

2. Atienda la anotación `@ResuestMapping` publicando el servicio en la URI indicada, limítelo a tipos de retorno String.
   
   Ejemplo:

    ~~~
    public class HelloController {

	    @RequestMapping("/")
	    public String index() {
		    return "Greetings from Spring Boot!";
	    }
    }
    ~~~

## Contenido

  - [Prerrequisitos](#prerrequisitos)
  - [Compilar](#compilar)
  - [Ejecutar](#ejecutar-de-forma-local)
  - [Uso](#uso)
  - [Diagramas](#diagramas)
  - [Resultados de las Pruebas](#resultados-de-las-pruebas)
  - [Generación de Javadoc](#generación-de-javadoc)
  - [Documentación en PDF realizada en LATEX](#documentación-en-PDF-realizada-en-LATEX)
  - [Autor](#autor)
  - [Licencia](#licencia)

## Prerrequisitos

Para el desarrollo del proyecto se utilizó **Maven** como una herramienta para la construcción y gestión del mismo, el código fue desarrollado con el lenguaje de programación **Java**; por lo tanto se requiere para su ejecución tener estas dos herramientas en las versiones especificadas a continuación.

  - Java versión 8 o superior
  
  - Maven versión 3.5 o superior 
    
## Compilar

Después de descargar o clonar el proyecto se debe utilizar el comando `mvn package` para generar el ejecutable .jar con los .class compilados.

![](img/package.PNG)

## Ejecutar de forma local

Para utilizar el programa se debe haber realizado previamente la compilación del ejecutable .jar y de los archivos .class, una vez se haya realizado esto se usa el comando `java -cp <classpath> edu.eci.arep.App <filepath>`.

Donde `classpath` es la ruta hacia el .jar o hacia la carpeta donde se encuentran los archivos .class y `filepath` es la ruta del archivo del cual se lee el conjunto de números.

**Para mejorar la lectura de los resultados, estos se redondearon a dos cifras decimales**

#### Ejecución con .jar

Se utilizó el comando `java -cp "target/areplab4-1.0-SNAPSHOT.jar;target/dependency/*" edu.eci.arep.App edu.eci.arep.demoService.HelloWebService`.

![](img/exec1.PNG)


#### Ejecución con .class

Se utilizó el comando `java -cp "target/classes;target/dependency/*" edu.eci.arep.App edu.eci.arep.demoService.HelloWebService`.

![](img/exec3.PNG)

Después de realizar la ejecución de cualquiera de las dos formas, se accede de forma local abriendo un web browser y dirigiéndose a la dirección http://localhost:35000.

![](img/exec2.PNG)

## Uso

Para hacer uso de la aplicación se debe realizar lo siguiente:

1.  Abrir la aplicación de forma local o remota y escribir números separados por coma.

    URL Aplicación ejecutada de forma Local: http://localhost:35000

    URL Aplicación alojada de forma Remota con Heroku: https://arep-walteros-lab4.herokuapp.com/
    
    ![](img/use1.PNG)
    
    Esta página HTML contiene una imagen PNG y utiliza un archivo JS, estos son los tres tipos de archivos estáticos que tolera el servidor.
    
2.  Al hacer click en el botón `Toogle Image` la imagen desaparecerá.

    ![](img/use2.PNG)

3.  Si escribes en el cuadro de texto y haces click en el botón `Get Greeting` recibirás un saludo con el nombre que pusiste.

    ![](img/use3.PNG)
    
    Esta función utiliza al endpoint `/Apps/hello?value=Daniel` generada con el framework propio NanoSpark.
    
4.  Para acceder directamente a este endpoint basta con poner ese valor en la URL.

    ![](img/use4.PNG)
    
5.  El servidor retorna archivos estáticos con las extensiones js, png y html.
      
    ![](img/use5.PNG)
    
    ![](img/use6.PNG)
    
    ![](img/use7-1.PNG)
    
6.  En caso de que los archivos no existan se visualizará lo siguiente:

    ![](img/use8.PNG)

## Diagramas

![](diagrams/AppClassDiagram.png)

El programa principal utiliza la clase **NanoSpringApplication**, esta clase por medio de reflexión carga los componentes y su maneja su ejecución por medio de la interfaz **HttpServer**, su implementación crea por medio de sockets un servidor sobre el cual corre una aplicación web.

La clase **NanoSpringApplication** utiliza el método run para cargar los componentes recibidos en los argumentos del main usando reflexión, esto hace que no haya necesidad de modificar el codigo del framework para incorporar nuevos endpoints siempre y cuando utilicen la anotación **RequestMapping** en el método del nuevo endpoint y la anotación **PathVariable** en caso de que el path necesite variables.

![](diagrams/DemoClassDiagram.png)

Para probar el framework se creo un servicio simple llamado **HelloWebService** que utiliza las anotaciones previas y además por medio de la interfaz **PersistenceService** accede a una base de datos, su implemenración se conecta a una base de datos Firebase desarrollada para probar en tiempo real la conexión.

![](diagrams/ComponentDiagram.png)

La aplicación se divide en tres componentes principales, FrontEnd, BackEnd y FirebaseDB.

El componente más funcional de FrontEnd es **App**, este es el que lee la información que registra el usuario y por medio de Axios accede a **NanoSpringApplication** para usar el endpoint definido en NanoSpring.

Este endpoint se configuró una conexión con el componente **PersistenceServiceImpl**, este se conecta la base de datos Firebase para obtener el saludo que retorna junto con el nombre del usuario.

![](diagrams/deploy.PNG)

Debido a que utilizando que la aplicación desarrollada intenta emular el comportamiento de una aplicación web realizada con Spring, cualquier persona con conexión a internet puede acceder a la aplicación desplegada, la interacción del cliente con el servidor se realiza únicamente por el protocolo HTTP; por otra parte, la conexión del servidor con la base de datos firebase se realiza por el protocolo HTTPS.  

## Resultados de las Pruebas

El programa fue probado con seis pruebas unitarias de JUnit donde se contemplaron los siguientes casos:

  - Búsqueda de un archivo HTML.
  - Búsqueda de un archivo PNG.
  - Búsqueda de un archivo JS.
  - Búsqueda de un archivo inexistente.
  - Uso de Endpoint Generado Con NanoSpring.
  - Fallo Por Uso Erróneo de Endpoint Generado Con NanoSpring.

Los resultados de las pruebas se pueden visualizar al utilizar el comando `mvn package` o el comando `mvn test`.

![](img/test.PNG)

## Generación de Javadoc

Para generar la documentación de Java se utiliza el comando `mvn javadoc:javadoc`, la documentación se almacenará en el directorio `target/site/apidocs`.

![](img/javadoc.PNG)

**La documentación de Java de este proyecto se encuentra previamente en la carpeta docs/apidocs**

**Adicionalmente se puede acceder a una visualización de esta documentación accediendo a este [ENLACE](https://silenrate.github.io/AREP-Lab4/apidocs/)**

## Documentación en PDF realizada en LATEX

[Taller De Arquitecturas De Servidores De Aplicaciones, Meta Protocolos De Objetos, Patrón IOC y Reflexión](TallerClientesYServicios.pdf)

## Autor

  - **Daniel Felipe Walteros Trujillo**

## Licencia

Este proyecto está licenciado bajo la licencia **General Public License v3.0**, revise el archivo [LICENSE](LICENSE) para más detalles.
