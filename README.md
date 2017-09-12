# gendapter

The project is a playground for me learning about annotation processing and generating code. The 
first version should be able to generate simple abstract `RecyclerView.Adapter` class capable to consume 
items, and corresponding `RecyclerView.ViewHolder` class. Both generated classes are abstract to let the user define a layout
and binding implementation for the items. In the later versions I'm planning to add `Header` handling
capabilities as well as possibility to define `DiffUtils`

PRs are welcome, but since I got some ideas on further implementations, please file an issue before
making one so we could discuss.
