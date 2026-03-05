defmodule GameRules do
  @type t :: %__MODULE__{
          initial_feeds: list(),
          shop: list(),
          dice: list(),
          actions: map()
        }

  defstruct initial_feeds: [], shop: [], dice: [], actions: %{}

  @initial_feeds [
    Feed.grass(),
    Feed.grass(),
    Feed.grass(),
    Feed.grass(),
    Feed.apple1(),
    Feed.apple1(),
    Feed.corn1(),
    Feed.apple2()
  ]

  @primary_feeds [
    Feed.apple1(),
    Feed.apple2(),
    Feed.apple4(),
    Feed.corn1(),
    Feed.corn2(),
    Feed.corn4(),
    Feed.berry1(),
    Feed.berry2(),
    Feed.berry4(),
    Feed.cabbage1(),
    Feed.cabbage2(),
    Feed.cabbage4(),
    Feed.radish()
  ]

  @extended_feeds @primary_feeds ++ [Feed.prune(), Feed.carrot()]

  @white_dice [
    {:move, 1},
    {:move, 3},
    {:add_feed, Feed.apple2()},
    {:add_feed, Feed.cabbage2()},
    {:add_gem, 1},
    :pick_feed
  ]

  @yellow_dice [{:move, 0}, {:move, 1}, {:move, 1}, {:move, 2}, {:move, 3}, {:move, 4}]

  @basic_actions %{
    apple: :add_gem_by_vortex,
    corn: :roll_dice,
    berry: :move_to_next_vortex,
    cabbage: :put_back_feed,
    prune: :move_by_gem,
    carrot: :add_clover_by_vortex
  }

  @advanced_actions %{
    apple: :add_gem_by_price,
    corn: :roll_dice_by_vortex,
    berry: :upgrade_berry,
    cabbage: :pick_feed,
    prune: :give_feed_by_vortex,
    carrot: :add_clover_by_carrot_count
  }

  def default() do
    %__MODULE__{
      initial_feeds: @initial_feeds,
      shop: @extended_feeds,
      dice: @white_dice,
      actions: @basic_actions
    }
  end

  def with_primary_feeds(%__MODULE__{} = rules) do
    %__MODULE__{rules | shop: @primary_feeds}
  end

  def with_extended_feeds(%__MODULE__{} = rules) do
    %__MODULE__{rules | shop: @extended_feeds}
  end

  def with_white_dice(%__MODULE__{} = rules) do
    %__MODULE__{rules | dice: @white_dice}
  end

  def with_yellow_dice(%__MODULE__{} = rules) do
    %__MODULE__{rules | dice: @yellow_dice}
  end

  def with_basic_actions(%__MODULE__{} = rules) do
    %__MODULE__{rules | actions: @basic_actions}
  end

  def with_advanced_actions(%__MODULE__{} = rules) do
    %__MODULE__{rules | actions: @advanced_actions}
  end

  def get_action!(%__MODULE__{actions: actions}, %Feed{type: feed_type}) do
    Map.fetch!(actions, feed_type)
  end
end
