defmodule PlayerStrategy do
  @callback choose_free_feed(
              game_state :: GameState.t(),
              player_name :: binary(),
              feeds :: list()
            ) :: Feed.t()

  @callback choose_put_back_feed(
              game_state :: GameState.t(),
              player_name :: binary(),
              feeds :: list()
            ) :: Feed.t()

  @callback choose_buy_feed(
              game_state :: GameState.t(),
              player_name :: binary(),
              gem_count :: integer()
            ) :: Feed.t()
end
