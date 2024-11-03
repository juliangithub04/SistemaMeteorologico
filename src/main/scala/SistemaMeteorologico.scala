import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration._
import scala.util.Random

// Define modelo de datos meteorológicos
case class DatosMeteorologicos(temperatura: Double, presion: Double, humedad: Double)
object DatosMeteorologicos {
  def generarAleatorio(): DatosMeteorologicos = DatosMeteorologicos(
    temperatura = Random.between(-10, 40),
    presion = Random.between(950, 1050),
    humedad = Random.between(0, 100)
  )
}

// Simulador de fuente de datos
def fuenteDatos(): Stream[DatosMeteorologicos] = Stream.continually(DatosMeteorologicos.generarAleatorio())

// Función de procesamiento y agregación funcional
def calcularPromedio(datos: Seq[DatosMeteorologicos]): DatosMeteorologicos = {
  val (temp, pres, hum, count) = datos.foldLeft((0.0, 0.0, 0.0, 0)) { case ((t, p, h, c), d) =>
    (t + d.temperatura, p + d.presion, h + d.humedad, c + 1)
  }
  DatosMeteorologicos(temp / count, pres / count, hum / count)
}

// Procesamiento concurrente
import scala.concurrent.ExecutionContext.Implicits.global

def procesarConcurrente(fuentes: Seq[Stream[DatosMeteorologicos]]): Future[DatosMeteorologicos] = {
  val futuros = fuentes.map { fuente =>
    Future {
      val datos = fuente.take(100).toList  // Ejemplo de toma de 100 muestras por fuente
      calcularPromedio(datos)
    }
  }
  Future.sequence(futuros).map { resultados =>
    calcularPromedio(resultados)
  }
}

// Punto de entrada principal
object SistemaMeteorologico {
  def main(args: Array[String]): Unit = {
    val fuentes = Seq.fill(5)(fuenteDatos())  // Crea 5 fuentes simuladas
    val promedioFuturo = procesarConcurrente(fuentes)

    // Esperar el resultado del Future
    val promedio = Await.result(promedioFuturo, 10.seconds)
    println(s"Promedio de datos meteorológicos: $promedio")
  }
}
