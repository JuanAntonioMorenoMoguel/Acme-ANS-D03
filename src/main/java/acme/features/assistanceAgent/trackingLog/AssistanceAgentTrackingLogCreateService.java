
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.IndicatorType;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean exist;
		TrackingLog trackingLog;
		AssistanceAgent assistanceAgent;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		exist = trackingLog != null;
		if (exist) {
			assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
			if (assistanceAgent.equals(trackingLog.getClaims().getAssistanceAgents()))
				super.getResponse().setAuthorised(true);
		}
	}

	@Override
	public void load() {
		TrackingLog trackingLog = new TrackingLog();

		// Validamos si claimId está presente en la solicitud
		if (!super.getRequest().hasData("claimId"))
			throw new IllegalStateException("claimId no está presente en la solicitud.");

		int claimId = super.getRequest().getData("claimId", int.class);
		Claim claim = this.repository.findClaimById(claimId);

		// Validamos si el claim existe antes de continuar
		if (claim == null)
			throw new IllegalStateException("El Claim con ID " + claimId + " no existe.");

		Date updateMoment = MomentHelper.getCurrentMoment();
		trackingLog.setClaims(claim);
		trackingLog.setDraftMode(true);
		trackingLog.setUpdateMoment(updateMoment);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		trackingLog.setUpdateMoment(MomentHelper.getCurrentMoment());
		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolutionReason");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean allTrackingLogsPublished;
		int claimId;
		Collection<TrackingLog> trackingLogs;

		allTrackingLogsPublished = true;
		claimId = trackingLog.getClaims().getId();
		trackingLogs = this.repository.findLatestTrackingLogsByClaimId(claimId);

		for (TrackingLog element : trackingLogs)
			if (element.isDraftMode())
				allTrackingLogsPublished = false;

		if (!allTrackingLogsPublished)
			super.state(allTrackingLogsPublished, "*", "assistance-agent.tracking-log.form.error.allTrackingLogsPublished");

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices indicators;
		Dataset dataset;

		indicators = SelectChoices.from(IndicatorType.class, trackingLog.getIndicator());
		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolutionReason");

		dataset.put("indicators", indicators);

		super.getResponse().addData(dataset);
	}
}
