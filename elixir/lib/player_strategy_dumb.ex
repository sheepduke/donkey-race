defmodule PlayerStrategy.Dumb do
  @behaviour PlayerStrategy

  @doc """
  Randomly pick one.
  """
  def choose_free_feed(_game_state, _player_name, feeds) do
    Enum.random(feeds)
  end

  @doc """
  Choose the most expensive one.
  """
  def choose_put_back_feed(_game_state, _player_name, feeds) do
    Enum.max_by(feeds, & &1.price)
  end

  # Strategy for basic rule set:
  # 1. Buy the radish.
  # 2. Buy prune.
  # 3. Buy berry, apple, corn, cabbage.
  def choose_buy_feed(game_state, _player_name, gem_count) do
    if gem_count >= Feed.radish() do
      Feed.radish()
    end
  end
end
