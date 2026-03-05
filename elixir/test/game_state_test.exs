defmodule GameStateTest do
  use ExUnit.Case

  @ox Player.new("ox", nil)
  @sheep Player.new("sheep", nil)
  @game_state GameState.new(
                [@ox, @sheep],
                GameMap.short_map(),
                GameRules.default()
              )

  test "last_player?" do
    game_state =
      GameState.forward_player!(@game_state, "sheep", 3)
      |> GameState.forward_player!("ox", 3)

    assert GameState.last_player?(game_state, "sheep") == true
    assert GameState.last_player?(game_state, "ox") == true
  end

  test "get_winner" do
    game_state = GameState.forward_player!(@game_state, "sheep", 51)

    assert GameState.get_winner(game_state) == "sheep"
  end
end
