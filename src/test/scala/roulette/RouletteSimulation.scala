package roulette

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RouletteSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://games.alexecollins.com")
    .contentTypeHeader("application/json")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Classic Slot")
    .exec(http("Homepage").get("").check(status.is(200)))
    .exec(http("Get Game").get("/api/games/roulette").check(status.is(200)))
    .pause(7)
    .exec(http("Add Bet").post("/api/games/roulette/bets/red").body(StringBody("{\"amount\": 1}")).check(status.is(201)))
    .exec(http("Add Bet").post("/api/games/roulette/bets/black").body(StringBody("{\"amount\": 1}")).check(status.is(201)))
    .exec(http("Add Bet").post("/api/games/roulette/bets/number").body(StringBody("{\"amount\": 1, \"number\": 2}")).check(status.is(201)))
    .exec(http("Spin").post("/api/games/roulette/spins").body(StringBody("{}")).check(status.is(201)))
    .pause(3)

  setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))
}