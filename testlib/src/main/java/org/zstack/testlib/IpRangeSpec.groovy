package org.zstack.testlib

import org.zstack.sdk.IpRangeInventory

/**
 * Created by xing5 on 2017/2/15.
 */
class IpRangeSpec extends Spec implements HasSession {
    String name = "ip-range"
    String description
    String startIp
    String endIp
    String netmask
    String gateway

    IpRangeInventory inventory

    IpRangeSpec(EnvSpec envSpec) {
        super(envSpec)
    }

    SpecID create(String uuid, String sessionId) {
        inventory = addIpRange {
            delegate.resourceUuid = uuid
            delegate.name = name
            delegate.description = description
            delegate.startIp = startIp
            delegate.endIp = endIp
            delegate.netmask = netmask
            delegate.gateway = gateway
            delegate.sessionId = sessionId
            delegate.l3NetworkUuid = (parent as L3NetworkSpec).inventory.uuid
        }

        postCreate {
            inventory = queryIpRange {
                conditions=["uuid=${inventory.uuid}".toString()]
            }[0]
        }

        return id(name, inventory.uuid)
    }

    @Override
    void delete(String sessionId) {
        if (inventory != null) {
            deleteIpRange {
                delegate.uuid = inventory.uuid
                delegate.sessionId = sessionId
            }

            inventory = null
        }
    }
}