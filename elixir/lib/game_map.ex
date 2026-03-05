defmodule GameMap do
  @type t :: %__MODULE__{
          length: integer(),
          vortexes: list()
        }

  defstruct length: nil, vortexes: []

  def new(length, vortexes)
      when is_integer(length) and length >= 0 and
             is_list(vortexes) and is_integer(hd(vortexes)) and hd(vortexes) > 0 do
    %__MODULE__{length: length, vortexes: vortexes}
  end

  def short_map() do
    %__MODULE__{
      length: 50,
      vortexes: [2, 4, 7, 11, 14, 17, 21, 24, 27, 30, 34, 36, 39, 42, 44, 47]
    }
  end

  def on_vortex?(%__MODULE__{vortexes: vortexes}, position) do
    Enum.member?(vortexes, position)
  end

  @doc """
  Return a number as the next vortex starting from `position`.
  Return `position` when there is none.
  """
  def next_vortex(%__MODULE__{vortexes: vortexes}, position) do
    Enum.find(vortexes, position, &(&1 > position))
  end
end
