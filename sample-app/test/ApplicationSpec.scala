import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  "Messages" should {
    "be within namespace" in new WithApplication {
      val greeting = route(FakeRequest(GET, "/messages/users.greeting")).get
      status(greeting) must equalTo(OK)
      contentAsString(greeting) must contain ("Welcome!")

      val goodbye = route(FakeRequest(GET, "/messages/users.goodbye/German")).get
      status(goodbye) must equalTo(OK)
      contentAsString(goodbye) must contain ("Bye, German")
    }
  }
}
