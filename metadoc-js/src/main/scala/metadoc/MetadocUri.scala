package metadoc

import monaco.Uri

sealed trait UriAnchor

object UriAnchor {
  case class Position(line: Int, column: Option[Int])


  case class Symbol(symbol: String) extends UriAnchor
  case class Range(start: Position, end: Option[Position]) extends UriAnchor
}

case class MetadocUri(base: Uri, anchor: Option[UriAnchor]) {
  def toUri: Uri = {
    base
  }
}

object MetadocUri {

  import fastparse.all._

  lazy val parser: P[(String, Option[UriAnchor])] = {

    val path: P[String] =
      (!":" ~ AnyChar).rep.!

    val symbol: P[UriAnchor.Symbol] =
      "@" ~ AnyChar.rep.!.map(symbol => UriAnchor.Symbol(symbol))

    // Range
    val digit = CharIn('0' to '9').!
    val number: P[Int] = digit.rep(1).!.map(_.toInt)

    val position: P[UriAnchor.Position] = ("L" ~ number ~ ("C" ~ number).?).map{
      case(line, column) => UriAnchor.Position(line, column)
    }

    val range: P[UriAnchor.Range] = (position ~ ("-" ~ position).?).map{
      case (start, end) => UriAnchor.Range(start, end)
    }

    val anchor: P[UriAnchor] = ":" ~ (symbol | range)

    path ~ anchor.?
  }

  def fromUri(fullUri: Uri): MetadocUri = {
    parser.parse(fullUri.fragment) match {
      case Parsed.Success((path, anchor), _) =>
        MetadocUri(fullUri.withFragment(path), anchor)
      case _ =>
        MetadocUri(fullUri, None)
    }
  }

  def fromString(fullUri: String): MetadocUri =
    MetadocUri.fromUri(createUri(fullUri))

}