from com.fasterxml.jackson.databind.node import NullNode
from org.onap.ccsdk.apps.controllerblueprints.core.utils import JacksonUtils
from org.onap.ccsdk.apps.controllerblueprints.core import BluePrintTypes
from java.lang import Exception
from java.util import Date
from blueprint_constants import *


class ResourceAssignmentUtils:

    @classmethod
    def set_ressource_data_value(cls, ra, runtime_exception, value):
        print "Processing 1..."
        if ra.property is None:
            raise Exception("Failed in setting resource value property for resource mapping " + ra)
        print "Processing 2..."
        if ra.name is None:
            raise Exception("Failed in setting resource value name for resource mapping " + ra)
        print "Processing 3..."
        if ra.dictionaryName is None:
            ra.dictionaryName = ra.name

        try:
            if ra.type is None:
                pass
            else:
                print "Processing 4..."
                json_node_value = cls.convert_resource_value(ra.type, value)
                print "Processing 5..."
                cls.set_resource_value(ra, runtime_exception, json_node_value)
                print "Processing 6..."
                ra.property.value = value
                ra.updatedDate = Date()
                ra.updatedBy = PROPERTY_BLUEPRINT_USER_SYSTEM
                ra.status = PROPERTY_BLUEPRINT_STATUS_SUCCESS
        except Exception as e:
            raise Exception("Failed in setting value for template key (" + ra.name + ") and " +
                            "dictionary key (" + ra.dictionaryName +
                            ") of type (" + ra.type + ") with error message (" + e.message + ")")
        return None

    @staticmethod
    def set_failed_resource_data_value(ra, message):
        if ra.name is not None:
            ra.updatedDate = Date()
            ra.updatedBy = PROPERTY_BLUEPRINT_USER_SYSTEM
            ra.status = PROPERTY_BLUEPRINT_STATUS_FAILURE
            ra.message = message
        else:
            pass
        return None

    @staticmethod
    def set_resource_value(ra, runtime_exception, value):
        runtime_exception.put_resolution_store(ra.name, value)
        runtime_exception.put_dictionary_store(ra.dictionaryName, value)
        return None

    @staticmethod
    def convert_resource_value(type, value):
        if value is None or value == NullNode:
            return NullNode.instance
        elif BluePrintTypes.validPrimitiveTypes().contains(type) and isinstance(value, str):
            JacksonUtils.convertPrimitiveResourceValue(type, value)
        elif isinstance(value, str):
            return JacksonUtils.jsonNode(value)
        else:
            return JacksonUtils.getJsonNode(value)
