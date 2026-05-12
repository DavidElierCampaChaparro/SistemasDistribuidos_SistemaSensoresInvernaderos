# FOR AGENTS
This file might clarify doubts but is not necessary since we already have specified architecture
in the AGENTS.md file.

# Requerimientos del proyecto de sistemas distribuidos, sexto semestre, ingenieria en software.

Sistema de sensores en invernadero
Un invernadero, aunque está aislado de los elementos del ambiente, sí puede ser afectado
indirectamente por los mismos, sobre todo por índices de humedad y de temperatura. Los dueños
de una serie de invernaderos en donde se cultivan flores y hortalizas, requieren tener una relación
de los índices de humedad y temperatura para poder aplicar acciones que prevengan la proliferación
de hongos y plagas, o bien, que prevengan la pérdida de producto. Para ello compraron varios
sensores que transmiten a un Gateway las medidas de humedad y temperatura, y a su vez, este
Gateway es capaz de reenviar los datos a un servidor vía TCP en formato binario. Los dueños
requieren de un sistema que capture esos datos y que ofrezca la siguiente funcionalidad:
* Se necesita registrar los sensores de los cuales se recibirán los datos.
* Reportes gráficos por fecha del comportamiento de las medidas de humedad y
temperatura de distintos sensores y en promedio de un invernadero.
* Capacidad de programar alarmas de tal manera que, a cierta temperatura o índice de
humedad, se le envíe un correo electrónico o notificación al móvil sobre dicha alarma,
para que se tomen las medidas necesarias.
* Se requiere que los datos de los sensores se expongan de tal manera que sistemas de
estadística e inteligencia de negocios puedan recuperar información periódicamente para
hacer estadísticas o modelos predictivos.
* Se requiere que el sistema tenga la capacidad de poder soportar distintas marcas de
sensores, ya que la manera en la que se envía la información puede variar entre los
distintos fabricantes.
