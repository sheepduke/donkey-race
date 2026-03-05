defmodule GameEngine do
  def run(%GameState{} = game_state, player_name) when is_binary(player_name) do
    player = GameState.get_player!(game_state, player_name)
    {feed, player} = Player.pick_random_feed(player)

    # Update the player after picking a feed.
    # Move it forward.
    game_state =
      GameState.update_player!(game_state, player_name, fn _ -> player end)
      |> GameState.forward_player!(player_name, feed.value)

    # Act based on the feed.
    if feed.type == :grass do
      player = Player.inc_sleep(player)

      if player.sleep_count >= 3 do
        buy(game_state, player_name)
      else
        GameState.update_player!(game_state, player)
      end
    else
      action = GameRules.get_action!(game_state.rules, feed)
      Action.act(action, game_state, player_name)
    end
  end

  def buy(%GameState{} = game_state, player_name) do
    player =
      %Player{strategy: strategy, clover_count: clover_count} =
      GameState.get_player!(game_state, player_name)

    # First move forward X steps where X is the number of clover.
    # Then reset:
    # - used feeds
    # - sleep count
    # - gem count
    game_state = GameState.forward_player!(game_state, player_name, clover_count)

    gem_count =
      if GameState.last_player?(game_state, player_name),
        do: player.gem_count + 1,
        else: player.gem_count

    player =
      Player.reset_feeds(player) |> Player.reset_sleep() |> Player.reset_gem()

    GameState.update_player!(game_state, player)

    # Buy feed.
    feed_to_buy = strategy.choose_buy_feed(game_state, player_name, gem_count)
    player = Player.add_feed(player, feed_to_buy)

    GameState.update_player!(game_state, player)
  end
end
