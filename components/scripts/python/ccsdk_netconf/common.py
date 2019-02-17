class ResolutionHelper:

  def __init__(self, component_function):
    self.component_function = component_function

  def resolve_and_generate_message_from_template_prefix(self, artifact_prefix):
    return self.component_function.resolveAndGenerateMessage(artifact_prefix)

  def resolve_and_generate_message(self, artifact_mapping, artifact_template):
    return self.component_function.resolveAndGenerateMessage(artifact_mapping,
                                                             artifact_template)
