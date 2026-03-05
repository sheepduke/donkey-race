defmodule ActionTest do
  use ExUnit.Case

  @player_name "donkey"
  @game_state GameState.new(
                [Player.new(@player_name, nil)],
                GameMap.short_map(),
                GameRules.default()
              )

  test "move" do
    game_state = Action.act({:move, 10}, @game_state, @player_name)

    assert Map.fetch!(@game_state.player_positions, @player_name) == 0
    assert Map.fetch!(game_state.player_positions, @player_name) == 10
  end

  test "add_gem" do
    game_state = Action.act({:add_gem, 10}, @game_state, @player_name)

    assert GameState.get_player!(game_state, @player_name) ==
             GameState.get_player!(@game_state, @player_name) |> Player.inc_gem(10)
  end

  test "add_feed" do
    game_state = Action.act({:add_feed, Feed.apple2()}, @game_state, @player_name)

    assert GameState.get_player!(game_state, @player_name) ==
             Player.new(@player_name, nil, [Feed.apple2()])
  end

  test "add_gem_by_vortex - on vortex" do
    game_state = GameState.place_player!(@game_state, @player_name, 7)

    player =
      Action.act(:add_gem_by_vortex, game_state, @player_name)
      |> GameState.get_player!(@player_name)

    assert player.gem_count == 2
  end

  test "add_gem_by_vortex - not on vortex" do
    game_state = GameState.place_player!(@game_state, @player_name, 6)

    player =
      Action.act(:add_gem_by_vortex, game_state, @player_name)
      |> GameState.get_player!(@player_name)

    assert player.gem_count == 1
  end
end
