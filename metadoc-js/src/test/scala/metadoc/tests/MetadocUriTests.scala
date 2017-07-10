package metadoc
package tests

import org.scalatest.FunSuite

class MetadocUriTests extends FunSuite {
  test("parses symbols") {
    val result = MetadocUri.parser.parse("/foo/bar:@scala.util.Try")
    val (path, anchor) = result.get.value
    assert(path == "/foo/bar")
    assert(anchor.contains(UriAnchor.Symbol("scala.util.Try")))
  }

  test("parses ranges") {
    val result = MetadocUri.parser.parse("/foo/bar:L2C0-L3C4")

    val (path, anchor) = result.get.value
    assert(path == "/foo/bar")
    assert(anchor.contains(UriAnchor.Range(
      start = UriAnchor.Position(
        line = 2,
        column = Some(0)
      ),
      end = Some(UriAnchor.Position(
        line = 3,
        column = Some(4)
      ))
    )))
  }
}
