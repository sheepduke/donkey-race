defmodule GameEngineTest do
  use ExUnit.Case

  @player_name "ox"
  @player Player.new(@player_name, PlayerStrategy.Dumb)
  @game_state GameState.new([@player], GameMap.short_map(), GameRules.default())

  test "run - basic - apple" do
    game_state =
      GameState.update_player!(@game_state, @player_name, fn player ->
        Player.add_feed(player, Feed.apple2())
      end)

    game_state = GameEngine.run(game_state, @player_name)
    player = GameState.get_player!(game_state, @player_name)

    assert GameState.player_position!(game_state, @player_name) == 2
    assert player == %Player{@player | used_feeds: [Feed.apple2()], gem_count: 2}
  end

  test "run - basic - cabbage" do
    game_state =
      GameState.update_player!(@game_state, @player_name, fn player ->
        Player.add_feed(player, Feed.cabbage2())
      end)
      |> GameEngine.run(@player_name)

    assert GameState.get_player!(game_state, @player_name) == %Player{
             @player
             | available_feeds: [Feed.cabbage2()]
           }
  end

  test "run - basic - berry" do
    game_state =
      GameState.update_player!(@game_state, @player_name, fn player ->
        Player.add_feed(player, Feed.berry2())
      end)
      |> GameEngine.run(@player_name)

    assert GameState.player_position!(game_state, @player_name) == 4

    assert GameState.get_player!(game_state, @player_name) == %Player{
             @player
             | used_feeds: [Feed.berry2()]
           }
  end

  test "run - basic - prune" do
    player = Player.inc_gem(@player, 10)

    game_state =
      GameState.update_player!(@game_state, @player_name, fn player ->
        Player.inc_gem(player, 10) |> Player.add_feed(Feed.prune())
      end)
      |> GameEngine.run(@player_name)

    player = GameState.get_player!(game_state, @player_name)

    assert GameState.player_position!(game_state, @player_name) == 15
    assert player == %Player{@player | used_feeds: [Feed.prune()], gem_count: 10}
  end
end
