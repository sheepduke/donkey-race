defmodule Action do
  def act({:move, step}, %GameState{} = game_state, player_name) do
    GameState.forward_player!(game_state, player_name, step)
  end

  def act({:add_gem, count}, %GameState{} = game_state, player_name) do
    GameState.update_player!(game_state, player_name, fn player ->
      Player.inc_gem(player, count)
    end)
  end

  def act({:add_feed, feed}, %GameState{} = game_state, player_name) do
    GameState.update_player!(game_state, player_name, &Player.add_feed(&1, feed))
  end

  def act(:add_gem_by_vortex, %GameState{map: game_map} = game_state, player_name) do
    gem_count =
      if GameMap.on_vortex?(game_map, GameState.player_position!(game_state, player_name)),
        do: 2,
        else: 1

    GameState.update_player!(game_state, player_name, &Player.inc_gem(&1, gem_count))
  end

  def act(:roll_dice, %GameState{} = game_state, player_name) do
    # TODO
  end

  def act(:move_to_next_vortex, %GameState{map: game_map} = game_state, player_name) do
    current_position = GameState.player_position!(game_state, player_name)
    next_position = GameMap.next_vortex(game_map, current_position)
    GameState.place_player!(game_state, player_name, next_position)
  end

  def act(:put_back_feed, %GameState{} = game_state, player_name) do
    player = GameState.get_player!(game_state, player_name)
    feed = player.strategy.choose_put_back_feed(game_state, player_name, player.used_feeds)

    GameState.update_player!(
      game_state,
      player |> Player.remove_used_feed(feed) |> Player.add_feed(feed)
    )
  end

  def act(:move_by_gem, %GameState{} = game_state, player_name) do
    %Player{gem_count: gem_count} = GameState.get_player!(game_state, player_name)
    GameState.forward_player!(game_state, player_name, gem_count)
  end

  def act(:add_clover_by_vortex, %GameState{} = game_state, player_name) do
    # TODO
  end

  def act(:add_gem_by_price, %GameState{} = game_state, player_name) do
    # TODO
  end

  def act(:roll_dice_by_vortex, %GameState{} = game_state, player_name) do
    # TODO
  end

  def act(:upgrade_berry, %GameState{} = game_state, player_name) do
    # TODO
  end

  def act(:pick_feed, %GameState{} = game_state, player_name) do
    # TODO
  end

  def act(:give_feed_by_vortex, %GameState{} = game_state, player_name) do
    # TODO
  end

  def act(:add_clover_by_carrot_count, %GameState{} = game_state, player_name) do
    # TODO
  end
end
