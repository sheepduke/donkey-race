defmodule Player do
  @type t :: %__MODULE__{
          name: binary(),
          available_feeds: list(),
          used_feeds: list(),
          gem_count: integer(),
          sleep_count: integer(),
          clover_count: integer(),
          strategy: atom()
        }

  defstruct name: nil,
            available_feeds: [],
            used_feeds: [],
            gem_count: 0,
            sleep_count: 0,
            clover_count: 0,
            strategy: nil

  defguard is_player(term) when is_struct(term, __MODULE__)

  def new(name, strategy)
      when is_binary(name) and is_atom(strategy) do
    %__MODULE__{
      name: name,
      strategy: strategy
    }
  end

  def new(name, strategy, feeds)
      when is_binary(name) and is_atom(strategy) and is_list(feeds) do
    %__MODULE__{
      name: name,
      strategy: strategy,
      available_feeds: feeds
    }
  end

  def inc_gem(%__MODULE__{} = player) do
    inc_gem(player, 1)
  end

  def inc_gem(%__MODULE__{} = player, count) when is_integer(count) and count > 0 do
    %__MODULE__{player | gem_count: player.gem_count + count}
  end

  def reset_gem(%__MODULE__{} = player) do
    %__MODULE__{player | gem_count: 0}
  end

  def inc_clover(%__MODULE__{} = player) do
    inc_clover(player, 1)
  end

  def inc_clover(%__MODULE__{} = player, count) when is_integer(count) and count > 0 do
    %__MODULE__{player | clover_count: player.clover_count + count}
  end

  def inc_sleep(%__MODULE__{} = player) do
    %__MODULE__{player | sleep_count: player.sleep_count + 1}
  end

  def reset_sleep(%__MODULE__{} = player) do
    %__MODULE__{player | sleep_count: 0}
  end

  def add_feed(%__MODULE__{} = player, feed) when is_struct(feed, Feed) do
    %__MODULE__{player | available_feeds: [feed | player.available_feeds]}
  end

  def add_feeds(%__MODULE__{} = player, feeds) when is_list(feeds) do
    %__MODULE__{player | available_feeds: player.available_feeds ++ feeds}
  end

  def remove_used_feed(%__MODULE__{used_feeds: used_feeds} = player, feed)
      when is_struct(feed, Feed) do
    index = Enum.find_index(used_feeds, &(&1 == feed))
    used_feeds = List.delete_at(used_feeds, index)

    %__MODULE__{player | used_feeds: used_feeds}
  end

  def reset_feeds(%__MODULE__{available_feeds: available_feeds, used_feeds: used_feeds} = player) do
    available_feeds = Enum.concat([available_feeds, used_feeds]) |> Enum.sort()

    %__MODULE__{player | available_feeds: available_feeds, used_feeds: []}
  end

  @doc """
  Randomly pick an available feed, put it in used_feeds. Return {random_feed, new_player}.
  """
  def pick_random_feed(
        %__MODULE__{available_feeds: available_feeds, used_feeds: used_feeds} = player
      ) do
    index = :rand.uniform(Enum.count(available_feeds)) - 1
    {feed, new_feeds} = List.pop_at(available_feeds, index)
    {feed, %__MODULE__{player | available_feeds: new_feeds, used_feeds: [feed | used_feeds]}}
  end

  @doc """
  Find `old_feed` in `player.used_feeds`, and replace it with `new_feed`.
  """
  def replace_used_feed(%__MODULE__{used_feeds: feeds} = player, old_feed, new_feed) do
    index = Enum.find_index(feeds, &(&1 == old_feed))
    feeds = List.replace_at(feeds, index, new_feed)

    %__MODULE__{player | used_feeds: feeds}
  end
end
