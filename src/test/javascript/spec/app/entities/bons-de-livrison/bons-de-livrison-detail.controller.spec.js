'use strict';

describe('Controller Tests', function() {

    describe('Bons_de_livrison Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBons_de_livrison, MockCondition_de_reglement, MockClient, MockMode_de_paiment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBons_de_livrison = jasmine.createSpy('MockBons_de_livrison');
            MockCondition_de_reglement = jasmine.createSpy('MockCondition_de_reglement');
            MockClient = jasmine.createSpy('MockClient');
            MockMode_de_paiment = jasmine.createSpy('MockMode_de_paiment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bons_de_livrison': MockBons_de_livrison,
                'Condition_de_reglement': MockCondition_de_reglement,
                'Client': MockClient,
                'Mode_de_paiment': MockMode_de_paiment
            };
            createController = function() {
                $injector.get('$controller')("Bons_de_livrisonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:bons_de_livrisonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
