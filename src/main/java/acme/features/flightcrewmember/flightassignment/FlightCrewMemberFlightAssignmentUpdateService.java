
package acme.features.flightcrewmember.flightassignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightassignment.AssignmentStatus;
import acme.entities.flightassignment.Duty;
import acme.entities.flightassignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		FlightAssignment assignment;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		status = assignment.getDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;
		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;
		Leg leg;
		int memberId;
		FlightCrewMember member;

		legId = super.getRequest().getData("flightLeg", int.class);
		leg = this.repository.findLegById(legId);
		memberId = super.getRequest().getData("member", int.class);
		member = this.repository.findMemberById(memberId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		assignment.setLeg(leg);
		assignment.setCrewMember(member);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		Collection<FlightCrewMember> members;
		SelectChoices selectedMembers;

		legs = this.repository.findAllLegs();
		members = this.repository.findAllCrewMembers();

		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(Duty.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getCrewMember());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);
		dataset.put("member", selectedMembers.getSelected().getKey());
		dataset.put("members", selectedMembers);

		super.getResponse().addData(dataset);
	}
}
